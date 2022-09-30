package ru.droidcat.roxie_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.droidcat.core_navigation.*
import ru.droidcat.core_ui.theme.RoxieTestTheme
import ru.droidcat.core_utils.collectWithLifecycle
import ru.droidcat.feature_taxi_api.intent.TaxiOrderChosen
import ru.droidcat.feature_taxi_impl.TaxiNavigationDestination
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationFactories: @JvmSuppressWildcards Set<NavigationFactory>

    @Inject
    lateinit var featureIntentManager: FeatureIntentManager

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            RoxieTestTheme {

                val navController = rememberNavController()

                var canPopUp by remember { mutableStateOf(false) }

                DisposableEffect(navController) {
                    val listener = NavController.OnDestinationChangedListener { controller, _, _ ->
                        canPopUp = controller.previousBackStackEntry != null
                    }
                    navController.addOnDestinationChangedListener(listener)
                    onDispose {
                        navController.removeOnDestinationChangedListener(listener)
                    }
                }

                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        modifier = Modifier.animateContentSize(),
                                        text = stringResource(R.string.app_name)
                                    )
                                },
                                navigationIcon = {
                                    AnimatedVisibility(
                                        visible = canPopUp,
                                        enter = scaleIn(),
                                        exit = scaleOut()
                                    ) {
                                        IconButton(onClick = { navController.navigateUp() }) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowBack,
                                                contentDescription = "Back"
                                            )
                                        }
                                    }
                                },
                                scrollBehavior = scrollBehavior,
                                windowInsets = WindowInsets.systemBars.only(
                                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal
                                )
                            )
                        },
                        contentWindowInsets = WindowInsets.systemBars.only(
                            WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal
                        )
                    ) { paddings ->
                        CompositionLocalProvider(LocalContentPaddings provides paddings) {
                            NavigationHost(
                                navController = navController,
                                factories = navigationFactories
                            )
                        }
                    }
                }

                featureIntentManager
                    .featureIntent
                    .collectWithLifecycle(
                        key = navController
                    ) { intent ->
                        onFeatureIntent(intent, navController)
                    }
            }
        }
    }

    private fun onFeatureIntent(
        intent: FeatureIntent,
        navController: NavController
    ) {
        when (intent) {
            is TaxiOrderChosen -> {
                navController.navigate(
                    route = "${TaxiNavigationDestination.RideDetails.route}/${intent.order_id}"
                ) {
                    launchSingleTop = true
                }
            }
        }
    }
}