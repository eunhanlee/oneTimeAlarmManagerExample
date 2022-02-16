package com.example.onetimealarmmanagerexample

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.PendingIntent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.onetimealarmmanagerexample.Constant.Companion.CHANNEL_DESCRIPTION
import com.example.onetimealarmmanagerexample.Constant.Companion.CHANNEL_ID
import com.example.onetimealarmmanagerexample.Constant.Companion.CHANNEL_NAME
import com.example.onetimealarmmanagerexample.Constant.Companion.NOTIFICATION_DESCRIPTION
import com.example.onetimealarmmanagerexample.Constant.Companion.NOTIFICATION_ID
import com.example.onetimealarmmanagerexample.Constant.Companion.NOTIFICATION_TITLE

class MyReceiver : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        setNotification(context)
    }

    fun setNotification(context: Context) {
        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        createNotificationChannel()
        deliverNotification(context)

    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, // 채널의 아이디
                CHANNEL_NAME, // 채널의 이름
                NotificationManager.IMPORTANCE_HIGH
                /*
                    IMPORTANCE_HIGH -  알림음O진동O상태표시줄O내부신호O헤드업알림O
                    IMPORTANCE_DEFAULT-알림음O진동O상태표시줄O내부신호O
                    IMPORTANCE_LOW - 알림음X 진동X 상태 표시줄O 내부 신호O
                    IMPORTANCE_MIN - 알림음X 진동X 상태 표시줄X 내부 신호O
                 */
            )
            notificationChannel.enableLights(true) // 불빛
            notificationChannel.lightColor = Color.RED // 색상
            notificationChannel.enableVibration(true) // 진동 여부
            notificationChannel.description = CHANNEL_DESCRIPTION // 채널 정보
            notificationManager.createNotificationChannel(
                notificationChannel
            )
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent
            .getActivity(
                context,
                NOTIFICATION_ID, // requestCode
                contentIntent, // 알림 클릭 시 이동할 인텐트
                PendingIntent.FLAG_UPDATE_CURRENT
                /*
                1. FLAG_UPDATE_CURRENT : 현재 PendingIntent를 유지하고,
                대신 인텐트의 extra data는 새로 전달된 Intent로 교체
                2. FLAG_CANCEL_CURRENT : 현재 인텐트가 이미 등록되어있다면 삭제,
                다시 등록
                3. FLAG_NO_CREATE : 이미 등록된 인텐트가 있다면, null
                4. FLAG_ONE_SHOT : 한번 사용되면, 그 다음에 다시 사용하지 않음
                 */
            )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // 아이콘
            .setContentTitle(NOTIFICATION_TITLE) // 제목
            .setContentText(NOTIFICATION_DESCRIPTION) // 내용
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}