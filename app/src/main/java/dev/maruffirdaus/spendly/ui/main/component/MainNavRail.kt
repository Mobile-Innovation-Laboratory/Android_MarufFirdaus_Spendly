package dev.maruffirdaus.spendly.ui.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.ui.main.constant.MainNavItems
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@Composable
fun MainNavRail(
    selectedNavItem: MainNavItems,
    onNavRailItemClick: (MainNavItems) -> Unit,
    onMenuClick: () -> Unit,
    onAddActivityClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier,
        header = {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = stringResource(R.string.menu)
                )
            }

            if (selectedNavItem == MainNavItems.ACTIVITIES) {
                AddFab(
                    onClick = onAddActivityClick,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .offset(y = if (selectedNavItem == MainNavItems.ACTIVITIES) (-66).dp else (-36).dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            MainNavItems.entries.forEach { navItem ->
                val isSelected = navItem == selectedNavItem

                NavigationRailItem(
                    selected = isSelected,
                    onClick = {
                        onNavRailItemClick(navItem)
                    },
                    icon = {
                        Icon(
                            painter = if (isSelected) {
                                painterResource(navItem.selectedIcon)
                            } else {
                                painterResource(navItem.unselectedIcon)
                            },
                            contentDescription = stringResource(navItem.label)
                        )
                    },
                    label = {
                        Text(stringResource(navItem.label))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun MainNavRailPreview() {
    SpendlyTheme {
        MainNavRail(
            selectedNavItem = MainNavItems.ACTIVITIES,
            onNavRailItemClick = {},
            onMenuClick = {},
            onAddActivityClick = {},
        )
    }
}