package dev.maruffirdaus.spendly.ui.main.component

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.maruffirdaus.spendly.ui.main.constant.MainNavItems
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@Composable
fun MainNavBar(
    selectedNavItem: MainNavItems,
    onNavBarItemClick: (MainNavItems) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        MainNavItems.entries.forEach { navItem ->
            val isSelected = navItem == selectedNavItem

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onNavBarItemClick(navItem)
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

@Preview
@Composable
private fun MainNavBarPreview() {
    SpendlyTheme {
        MainNavBar(
            selectedNavItem = MainNavItems.HOME,
            onNavBarItemClick = {}
        )
    }
}