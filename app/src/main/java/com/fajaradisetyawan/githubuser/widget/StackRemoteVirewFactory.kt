package com.fajaradisetyawan.githubuser.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.fajaradisetyawan.githubuser.R
import com.fajaradisetyawan.githubuser.db.DatabaseContract.UserColum.Companion.CONTENT_URI
import com.fajaradisetyawan.githubuser.helper.MappingHelper
import com.fajaradisetyawan.githubuser.model.GithubUser

class StackRemoteVirewFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private var cursor: Cursor? = null
    private val items = ArrayList<GithubUser>()

    override fun onDataSetChanged() {
        if (cursor != null) {
            cursor?.close()
        }

        val identityToken = Binder.clearCallingIdentity()
        cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
        val list = MappingHelper.mapCursorToArrayList(cursor)
        items.addAll(list)
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onCreate() {
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.item_widget)
        try {
            val bitmap = Glide.with(context)
                    .asBitmap()
                    .load(items[position].avatar_url)
                    .submit()
                    .get()
            rv.setImageViewBitmap(R.id.img_widget, bitmap)
        }catch (e: InterruptedException){
            e.printStackTrace()
        }catch (e: InterruptedException){
            e.printStackTrace()
        }

        val extras = bundleOf(FavoriteWidget.EXTRA_ITEM to position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.img_widget, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun getCount(): Int = items.size

    override fun getViewTypeCount(): Int  = 1

    override fun onDestroy() {
    }
}