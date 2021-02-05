package com.fajaradisetyawan.githubuser.widget

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import com.fajaradisetyawan.githubuser.R
import www.sanju.motiontoast.MotionToast

class FavoriteWidget : AppWidgetProvider() {
    companion object{
        private const val TOAST_ACTION = "com.fajaradisetyawan.githubuser.TOAST_ACTION"
        const val EXTRA_ITEM = "com.fajaradisetyawan.githubuser.EXTRA_ITEM"

        private fun updateAppWidget(
                context: Context,
                appWidgetManager: AppWidgetManager,
                appWidgetId: Int
        ){
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            // construct remoteview
            val views = RemoteViews(
                    context.packageName,
                    R.layout.stack_widget
            )
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.tv_fav_empty)

            val toastIntent = Intent(context, FavoriteWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetIds in appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetIds)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action != null){
            val viewIndex = intent.getIntExtra(EXTRA_ITEM,0)
            Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
        }
    }
}