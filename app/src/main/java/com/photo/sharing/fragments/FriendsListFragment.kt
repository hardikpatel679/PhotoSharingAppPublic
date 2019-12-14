package com.photo.sharing.fragments


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.photo.sharing.R
import com.photo.sharing.adapters.FriendsListAdapter
import com.photo.sharing.enumconstants.EnvironmentKey
import com.photo.sharing.extensions.createUUID
import com.photo.sharing.extensions.displaySingleChoiceDialog
import com.photo.sharing.extensions.gone
import com.photo.sharing.extensions.visible
import com.photo.sharing.model.ResourceState
import com.photo.sharing.model.UserFriends
import com.photo.sharing.utils.*
import com.photo.sharing.viewmodel.FriendsListViewModel
import kotlinx.android.synthetic.main.fragment_friends_list.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast
import java.io.File


class FriendsListFragment : Fragment() {


    lateinit var userFriendsList: List<UserFriends>
    private lateinit var viewModel: FriendsListViewModel
    private lateinit var friendsListAdapter: FriendsListAdapter

    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    private val permissionRequest = 22 // A random number

    private var selectedImage: Uri? = null


    private var isGalleryImageSelected = false

    private var fileName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_friends_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.title = getString(R.string.friend_list)
        initUI()
        setupRecyclerView()
        setUpViewModel()
    }

    private fun initUI() {
        fabBtn.setOnClickListener {
            requestPermissions(requiredPermissions, permissionRequest)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.changeUser -> {
                lifecycleScope.launch {
                    val usersInDB = viewModel.getUsers()
                    val users = arrayOfNulls<String>(usersInDB.size)
                    for (i in usersInDB.indices) {
                        users[i] = usersInDB[i].userName
                    }
                    val currentSelection =
                        CurrentEnvironment.getInt(EnvironmentKey.currentUserId) - 1

                    context?.displaySingleChoiceDialog(
                        getString(R.string.select_user),
                        users,
                        currentSelection
                    ) {
                        val selectedUserId = usersInDB[it].id
                        CurrentEnvironment.setInt(EnvironmentKey.currentUserId, selectedUserId)
                        viewModel.setFilterUserId(selectedUserId)
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        rvFriendsList.layoutManager = LinearLayoutManager(context)
        friendsListAdapter = FriendsListAdapter {
            val directions =
                FriendsListFragmentDirections.actionFriendsListFragmentToSharedImagesFragment(
                    it.userId,
                    it.friendId
                )
            findNavController().navigate(directions)
        }
        rvFriendsList.adapter = friendsListAdapter
    }

    private fun setUpViewModel() {
        viewModel =
            ViewModelProvider(this).get(FriendsListViewModel::class.java)

        viewModel.setFilterUserId(CurrentEnvironment.getInt(EnvironmentKey.currentUserId))

        viewModel.getUsersFriendsList().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                userFriendsList = it
                friendsListAdapter.updateList(userFriendsList)
                rvFriendsList.visible()
                tvNoRecords.gone()
                fabBtn.visible()
            } else {
                tvNoRecords.visible()
                rvFriendsList.gone()
                fabBtn.gone()
            }
        })

        viewModel.getImageUploadStatusLiveData().observe(viewLifecycleOwner, Observer {
            when (it) {
                ResourceState.LOADING -> {
                    ProgressDialogUtils.showProgressDialog(context!!)
                }
                ResourceState.SUCCESS -> {
                    ProgressDialogUtils.dismissDialog()
                    activity?.toast(getString(R.string.upload_completed))
                }
                ResourceState.ERROR -> {
                    ProgressDialogUtils.dismissDialog()
                    activity?.toast(getString(R.string.something_went_wrong))
                }
            }

        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            permissionRequest -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    val options = listOf(getString(R.string.camera), getString(R.string.gallery))
                    activity?.selector(
                        getString(R.string.select_option),
                        options
                    ) { selectedOption ->
                        when (selectedOption) {
                            1 -> {
                                ImageUtils.openGallery(this)
                                isGalleryImageSelected = true
                            }
                            0 -> {
                                fileName = ImageUtils.openCamera(this)
                                isGalleryImageSelected = false
                            }
                        }
                    }

                } else {
                    activity?.toast(getString(R.string.permission_required))
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            2 -> if (resultCode == Activity.RESULT_OK) {

                val file = File(Environment.getExternalStorageDirectory(), fileName)

                selectedImage =
                    FileProvider.getUriForFile(
                        activity!!,
                        activity!!.applicationContext.packageName + ".provider",
                        file
                    )

                showFriendSelectionDialog()
            }

            1 -> if (resultCode == Activity.RESULT_OK) {
                selectedImage = data?.data

                fileName = createUUID() + "." + FileUtils.getFileExtension(
                    selectedImage
                    , context
                )
                showFriendSelectionDialog()
            }
        }
    }

    private fun showFriendSelectionDialog() {
        val friendNames = arrayOfNulls<String>(userFriendsList.size)

        for (i in friendNames.indices) {
            friendNames[i] = userFriendsList[i].userName
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity!!)
        builder.setTitle(getString(R.string.share_with))

        builder.setMultiChoiceItems(
            friendNames, null
        ) { _, indexSelected, isChecked ->

            if (isChecked)
                viewModel.selectedUsers.add(userFriendsList[indexSelected].friendId)
            else
                viewModel.selectedUsers.remove(userFriendsList[indexSelected].friendId)

        }
        builder.setPositiveButton(
            getString(R.string.share)
        ) { dialog, _ ->
            dialog.dismiss()

            val fileObj = File(
                activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "/$fileName"
            )
            FileUtils.createFile(activity!!, selectedImage!!, fileObj)

            viewModel.uploadSelectedImageToAWS(
                fileName,
                fileObj,
                AWSConfiguration.getTransferUtilityBuilder(activity?.applicationContext)
            )
        }

        builder.setNegativeButton(
            getString(R.string.cancel)
        ) { dialog, _ -> dialog.dismiss() }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
