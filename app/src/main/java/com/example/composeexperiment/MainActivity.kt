package com.example.composeexperiment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.composeexperiment.data.models.User
import com.example.composeexperiment.ui.theme.ComposeExperimentTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.Dp
import com.example.composeexperiment.data.models.userList


class MainActivity : ComponentActivity() {

    private lateinit var userList: List<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        buildNotificationAnotherChannel(this@MainActivity)
        setContent {
            ComposeExperimentTheme {
                MainScreen(this, userList)
            }

        }
    }

    private fun buildNotificationAnotherChannel(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "general_notification",
                "General Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            // Configure the notification channel.
            notificationChannel.description = "General Channel"
            //notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            val notificationManager = ContextCompat.getSystemService(
                context!!,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}

@Composable
fun UserApplication(userProfiles:List<User> = userList, context:Context){

}


@Composable
fun MainScreen(context: Context?, userList: List<User>?) {
    Scaffold(
        topBar = { AppBar(context = context) },
        bottomBar = { BottomNavBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Toast.makeText(context, "You just clicked a Clickable", Toast.LENGTH_LONG)
                        .show()
                },
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "",
                    tint = MaterialTheme.colors.primaryVariant
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    )

    {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.LightGray
        ) {
            LazyColumn {
                items(userList!!) { user ->
                    ProfileCard(
                        name = user.name,
                        status = user.status,
                        profileImg = user.profileImg,
                        strokeColor = user.strokeColor
                    )
                }
            }
            /*Column() {
                for (user in userList!!) {
                    ProfileCard(
                        name = user.name,
                        status = user.status,
                        profileImg = user.profileImg,
                        strokeColor = user.strokeColor
                    )
                }
            }*/

        }
    }
}

@Composable
fun ProfileCard(name: String, status: String, profileImg: Int, strokeColor: Color) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfilePicture(profileImg, strokeColor, 72.dp)
            Column(
                modifier = Modifier
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ProfileContent(name, status)
            }
        }

    }
}

@Composable
fun ProfilePicture(profileImg: Int, strokeColor: Color, size: Dp) {
    Card(
        shape = CircleShape,
        border = BorderStroke(width = 2.dp, color = strokeColor),
        modifier = Modifier.padding(16.dp),
        elevation = 4.dp
    ) {
        Image(
            painter = painterResource(profileImg),
            contentDescription = "test",
            modifier = Modifier
                .size(size)
                .background(
                    brush = Brush.linearGradient(
                        listOf(Color.Red, Color.Blue)
                    )
                )
        )
    }
}

@Composable
fun ProfileContent(name: String, status: String) {
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(name, style = MaterialTheme.typography.h5)
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(status, style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
fun AppBar(context: Context?) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                Toast.makeText(context, "You just clicked a Clickable", Toast.LENGTH_LONG)
                    .show()
            }) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "",
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
            }
        },
        title = { Text("Messaging Application users") }
    )
}

@Composable
fun BottomNavBar() {
    BottomAppBar(
        modifier = Modifier
            .height(65.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
        cutoutShape = CircleShape,
        elevation = 22.dp,
        backgroundColor = Color.Green,
        content = { Text("This is a test") }
    )
}

@Composable
fun UserProfileDetailsScreen(userProfile: User = userList[0]/*, context: Context*/) {
    Scaffold(/*topBar = { AppBar(context = context) }*/) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                ProfilePicture(
                    profileImg = userProfile.profileImg,
                    strokeColor = userProfile.strokeColor,
                    248.dp
                )
                ProfileContent(name = userProfile.name, status = userProfile.status)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeExperimentTheme {
        MainScreen(context = null, userList = userList)
    }
}

@Preview(showBackground = true)
@Composable
fun UserDetailsPreviewScreen() {
    ComposeExperimentTheme {
        UserProfileDetailsScreen(userProfile = userList[0])
    }
}