package journal.gratitude.com.gratitudejournal

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.presently.settings.PresentlySettings
import dagger.hilt.android.AndroidEntryPoint
import journal.gratitude.com.gratitudejournal.util.reminders.NotificationScheduler
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //TODO biometric locking

    @Inject lateinit var settings: PresentlySettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Container()
        }

        createNotificationChannels()

        NotificationScheduler().configureNotifications(this, settings)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(ContainerActivity.CHANNEL_ID, getString(R.string.channel_name),  NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.description = getString(R.string.channel_description)
            notificationChannel.enableVibration(true)

            val backupChannel = NotificationChannel(ContainerActivity.BACKUP_STATUS_CHANNEL, getString(R.string.backup_channel_name), NotificationManager.IMPORTANCE_HIGH)
            backupChannel.description = getString(R.string.backup_channel_description)
            backupChannel.enableVibration(true)

            val notificationManager = getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannels(listOf(notificationChannel, backupChannel))

        }
    }

}