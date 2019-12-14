package com.photo.sharing.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.photo.sharing.R
import com.photo.sharing.adapters.SharedImagesAdapter
import com.photo.sharing.extensions.gone
import com.photo.sharing.extensions.visible
import com.photo.sharing.viewmodel.FriendsListViewModel
import kotlinx.android.synthetic.main.fragment_shared_images.*
import org.jetbrains.anko.alert


class SharedImagesFragment : Fragment() {

    private lateinit var adapter: SharedImagesAdapter
    private lateinit var viewModel: FriendsListViewModel

    private val arguments: SharedImagesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shared_images, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            getString(R.string.shared_images)
        setupRecyclerView()
        setUpViewModel()
    }

    private fun setupRecyclerView() {
        rvSharedImages.layoutManager = GridLayoutManager(context, 2)

        adapter = SharedImagesAdapter { sharedId ->
            showDeleteDialog(sharedId)
        }
        rvSharedImages.adapter = adapter
    }

    private fun setUpViewModel() {
        viewModel =
            ViewModelProvider(this).get(FriendsListViewModel::class.java)

        viewModel.setFilter(arguments.userId, arguments.friendId)

        viewModel.getUserSharedImages().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty())
                adapter.updateList(it)
            else {
                tvNoImagesShared.visible()
                rvSharedImages.gone()
            }
        })

    }

    private fun showDeleteDialog(sharedId: Int) {
        activity?.alert(getString(R.string.delete_image_msg)) {
            title(getString(R.string.delete))
            positiveButton(getString(R.string.yes)) {
                viewModel.deleteSharedPhoto(sharedId)
            }
            negativeButton(getString(R.string.No)) {
                this.dismiss()
            }
        }?.show()
    }
}
