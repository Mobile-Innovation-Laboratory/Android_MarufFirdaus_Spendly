package dev.maruffirdaus.spendly.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.ui.Settings
import dev.maruffirdaus.spendly.ui.SignIn
import dev.maruffirdaus.spendly.ui.common.component.TopGradient
import dev.maruffirdaus.spendly.ui.settings.component.SettingsTopAppBar
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit,
    navController: NavController = rememberNavController()
) {
    LaunchedEffect(Unit) {
        onEvent(SettingsEvent.OnRefreshUser)
        onEvent(SettingsEvent.OnRefreshDataSyncEnabled)
    }

    Scaffold(
        topBar = {
            SettingsTopAppBar(
                onBackClick = {
                    navController.popBackStack(
                        route = Settings,
                        inclusive = true
                    )
                }
            )
        }
    ) { innerPadding ->
        Box {
            TopGradient(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .height(16.dp)
                    .zIndex(1f)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.account),
                            color = MaterialTheme.colorScheme.onSurface,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = uiState.user?.email
                                ?: stringResource(R.string.not_signed_in),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    TextButton(
                        onClick = {
                            if (uiState.user == null) {
                                navController.navigate(
                                    SignIn(
                                        shouldEnableDataSync = false,
                                        shouldNavigateToMainScreen = false
                                    )
                                )
                            } else {
                                onEvent(SettingsEvent.OnShowSignOutDialog)
                            }
                        }
                    ) {
                        Text(
                            if (uiState.user == null) {
                                stringResource(R.string.sign_in)
                            } else {
                                stringResource(R.string.sign_out)
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.data_sync),
                            color = MaterialTheme.colorScheme.onSurface,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(R.string.enable_or_disable_data_sync),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Switch(
                        checked = uiState.isDataSyncEnabled && uiState.user != null,
                        onCheckedChange = {
                            if (it && uiState.user == null) {
                                navController.navigate(
                                    SignIn(
                                        shouldEnableDataSync = true,
                                        shouldNavigateToMainScreen = false
                                    )
                                )
                            } else {
                                onEvent(SettingsEvent.OnDataSyncEnabledChange(it))
                            }
                        }
                    )
                }
            }
        }

        if (uiState.isSignOutDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    onEvent(SettingsEvent.OnHideSignOutDialog)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(
                                SettingsEvent.OnSignOut(
                                    onSuccess = {
                                        onEvent(SettingsEvent.OnRefreshUser)
                                    }
                                )
                            )
                            onEvent(SettingsEvent.OnHideSignOutDialog)
                        }
                    ) {
                        Text(stringResource(R.string.yes))
                    }
                },
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .fillMaxWidth(),
                dismissButton = {
                    TextButton(
                        onClick = {
                            onEvent(SettingsEvent.OnHideSignOutDialog)
                        }
                    ) {
                        Text(stringResource(R.string.no))
                    }
                },
                title = {
                    Text(stringResource(R.string.sign_out_title))
                },
                text = {
                    Text(stringResource(R.string.sign_out_message))
                }
            )
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SpendlyTheme {
        SettingsScreen(
            uiState = SettingsUiState(),
            onEvent = {}
        )
    }
}