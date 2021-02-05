package com.fajaradisetyawan.githubuser.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.githubuser.R
import com.fajaradisetyawan.githubuser.adapter.GithubUserAdapter
import com.fajaradisetyawan.githubuser.model.GithubUser
import com.fajaradisetyawan.githubuser.view.activity.DetailActivity
import com.fajaradisetyawan.githubuser.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {
    companion object{
        private const val ARG_USERNAME = "username"

        fun getUsername(username: String): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var followingViewModel: FollowingViewModel
    private val githubUser = ArrayList<GithubUser>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME).toString()

        // setup recycle view
        rv_following.layoutManager = LinearLayoutManager(activity)
        rv_following.setHasFixedSize(true)

        val githubUserAdapter = GithubUserAdapter(githubUser)
        githubUserAdapter.notifyDataSetChanged()
        rv_following.adapter = githubUserAdapter

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(FollowingViewModel::class.java)

        followingViewModel.setFollowing(username)
        followingViewModel.getFollowing().observe(requireActivity(), Observer { users ->
            if ((users != null) && (users.size != 0)){
                githubUserAdapter.setGithubUser(users)
            }else{
                txt_following.text = username + resources.getString(R.string.no_following)
                rv_following.visibility = View.INVISIBLE
                no_following.visibility = View.VISIBLE
            }
        })

        followingViewModel.noConnection().observe(requireActivity(), Observer { result ->
            no_internet_connection_following.visibility = View.VISIBLE
            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show()
        })

        githubUserAdapter.setOnItemClickCallback(object : GithubUserAdapter.OnItemClickCallback{
            override fun onItemClicked(githubUser: GithubUser) {
                val detail = Intent(activity, DetailActivity::class.java)
                detail.putExtra(DetailActivity.EXTRA_DETAIL, githubUser)
                startActivity(detail)
            }
        })
    }

}