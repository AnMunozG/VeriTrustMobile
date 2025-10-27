package com.example.veritrustmobile.pantallas


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.veritrustmobile.rutas.Rutas
import com.example.veritrustmobile.data.accounts


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Acceder(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember {mutableStateOf(false)}

    var emailError by remember {mutableStateOf("") }
    var passwordError by remember {mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color.LightGray
                    )
                )
            )
    )

    Column (modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){


        // =========== TITULO DE LOGIN ===========


        Spacer(modifier = Modifier.height(150.dp))


        Text("Inicio de Sesión",
            style = TextStyle(
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                shadow = Shadow(
                    color = Color.Gray,
                    offset = Offset(1f, 1f),
                    blurRadius = 2f
                )
            )
        )

        Spacer(modifier = Modifier.height(25.dp))



        // =========== CAMPO DE TEXTO DE USUARIO ===========

        TextField(
            value = email,
            onValueChange = {
                if (it.length <= 25) email = it
            },
            label = { Text("Nombre")},
            leadingIcon = {
                Icon(Icons.Rounded.AccountCircle,
                    contentDescription = "",
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFF3F1F1)
            ),
            supportingText = {
                Row (
                    Modifier.fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {

                    if (emailError.isNotEmpty()) {
                        Text(
                            text = emailError,
                            color = Color.Red,
                            fontSize = 15.sp,
                        )
                    }

                    Text(
                        text = "${email.length} / 25",
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        fontSize = 15.sp
                    )
                }
            }

        )



        Spacer(modifier = Modifier.height(3.dp))



        // =========== CAMPO DE TEXTO DE CONTRASEÑA ===========

        TextField(
            value = password,
            onValueChange = {
                if (it.length <= 65) password = it
            },
            label = { Text("Contraseña") },
            leadingIcon = {
                Icon(Icons.Rounded.Lock, contentDescription = "")
            },
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(0xFFF3F1F1)
            ),
            supportingText = {
                Row (
                    Modifier.fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    if (passwordError.isNotEmpty()) {
                        Text(
                            text = passwordError,
                            color = Color.Red,
                            fontSize = 15.sp,
                        )
                    }

                    Text(
                        text = "${password.length} / 65",
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        fontSize = 15.sp
                    )
                }

            }
        )



        Spacer(modifier = Modifier.height(15.dp))



        // =========== BOTON DE LOGIN Y OLVIDASTE CONTRASEÑA ===========

        Button(
            onClick = {

                emailError = if (email.isBlank()) "Nombre no puede estar vacio" else ""
                passwordError = if (password.isBlank()) "Contraseña no puede estar vacia" else ""
                if (emailError.isEmpty() && passwordError.isEmpty()){
                    val user = accounts.find {it.user == email && it.password == password}

                    if (user != null){
                        navController.navigate(Rutas.Inicio.ruta+"/${user.user}")
                    }else {
                        loginError = "El usuario o contraseña no coinciden"
                    }

                }



            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 80.dp).height(50.dp),
            shape = RoundedCornerShape(25.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            ),
            colors = ButtonDefaults.buttonColors(
                Color.Black,
                Color.White
            )
        ) {
            Text(text = "Ingresar",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        if (loginError.isNotEmpty()){
            Text(
                text = loginError,
                color = Color.Red,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 17.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Olvidaste la contraseña?",
            color = Color.Black,
            modifier = Modifier.clickable {
                //logica de olvidar contraseña
            },
            fontSize = 16.sp
        )

        Text(text = "",)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAcceder() {
    Acceder(
        navController = rememberNavController()
    )
}
