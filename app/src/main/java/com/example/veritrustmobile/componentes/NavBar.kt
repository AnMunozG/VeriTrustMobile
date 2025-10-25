package com.example.veritrustmobile.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class NavDrawerItem(var title: String, var icon: ImageVector) {
    object Inicio : NavDrawerItem("Inicio", Icons.Default.Home)
    object Servicios : NavDrawerItem("Servicios", Icons.Default.Build)
    object Acceder : NavDrawerItem("Acceder", Icons.Default.AccountCircle)
    object Nosotros : NavDrawerItem("Nosotros", Icons.Default.Info)
}

@Composable
fun DrawerContent(
    scope: CoroutineScope,
    drawerState: DrawerState,
    onItemClick: (String) -> Unit
) {
    val items = listOf(
        NavDrawerItem.Inicio,
        NavDrawerItem.Servicios,
        NavDrawerItem.Acceder,
        NavDrawerItem.Nosotros
    )

    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {
            // Puedes agregar un encabezado aquí si lo deseas
            // por ejemplo, un logo o el nombre de la app.
            Text("Menú", modifier = Modifier.padding(bottom = 16.dp))
            Divider()
            items.forEach { item ->
                NavigationDrawerItem(
                    icon = { Icon(item.icon, contentDescription = null) },
                    label = { Text(item.title) },
                    selected = false, // Lo gestionaremos con la navegación real
                    onClick = {
                        scope.launch { drawerState.close() }
                        onItemClick(item.title)
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
