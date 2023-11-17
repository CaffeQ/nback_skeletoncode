package mobappdev.example.nback_cimpl.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.R
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameState
import mobappdev.example.nback_cimpl.ui.viewmodels.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel
import mobappdev.example.nback_cimpl.ui.viewmodels.Guess

@Composable
fun GameScreen(
    vm:GameViewModel,
    navigate: ()->Unit
){
    val gameState by vm.gameState.collectAsState()
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        //ChooseGameModes(vm)
        Grid(vm,gameState,sideLength = 3)
        VisualAndAudio(vm,navigate)

    }


}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun VisualAndAudio(vm: GameViewModel, navigate: () -> Unit){
    val gameState by vm.gameState.collectAsState()
    val visualButtonColor = when(gameState.guess){
        Guess.FALSE -> ButtonDefaults.buttonColors(Color.Red)
        Guess.CORRECT -> ButtonDefaults.buttonColors(Color.Green)
        else -> { ButtonDefaults.buttonColors(Color(127, 82, 255)) }
    }
    val audioButtonColor = ButtonDefaults.buttonColors(Color(127, 82, 255))


    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) })
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { vm.checkMatch() },
                colors = visualButtonColor
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sound_on),
                    contentDescription = "Sound",
                    modifier = Modifier
                        .height(48.dp)
                        .aspectRatio(3f / 2f)
                )
            }
            Button(
                onClick = { vm.checkMatch() },
                colors = audioButtonColor
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.visual),
                    contentDescription = "Visual",
                    modifier = Modifier
                        .height(48.dp)
                        .aspectRatio(3f / 2f)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier.background(Color.LightGray)
            ){
                Text(
                    modifier = Modifier.padding(12.dp),
                    text ="Score "+ vm.score.value.toString() + " N:"+vm.nBack.toString(),
                    color = Color.Black,
                )
            }
            StartGame(vm)
            GoHome(vm,navigate)
            ResetGame(vm)
        }
    }
}

@Composable
fun ResetGame(vm:GameViewModel){
    Button(
        onClick = vm::resetGame
    ){
        Text(
            modifier = Modifier.padding(2.dp),
            text = "Reset".uppercase(),
            color = Color.Black,
        )
    }
}

@Composable
fun Grid(vm: GameViewModel,gameState: GameState, sideLength: Int){
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        repeat(sideLength) { rowIndex ->
            Row(
            ) {
                repeat(sideLength) { columnIndex ->
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(
                                if (rowIndex * sideLength + columnIndex == gameState.eventValue) {
                                    Color(177, 253, 132)
                                } else {
                                    Color.LightGray
                                }
                            )
                    )
                }
            }
        }
    }
}
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DebugginText(vm:GameViewModel){
    val TAG ="TESTING"
    Text(text = "Event value: ${vm.gameState.value.eventValue}")
    Text(text = "Previous value: ${vm.gameState.value.previousValue}")
    Text(text = "Game type: ${vm.gameState.value.gameType}")

    Log.d(TAG,"Event value: "+vm.gameState.value.eventValue.toString())
    Log.d(TAG,"Previous value: ${vm.gameState.value.previousValue}")
    Log.d(TAG,"Game type: ${vm.gameState.value.gameType}")
}

@Composable
fun GetBoxColor(row: Int, column: Int,sideLength: Int ,eventValue:Int): Color {
    if(eventValue < 0 || eventValue > sideLength)
        return Color.LightGray
    return if ( row * sideLength + column == eventValue) {
        Color(177,253,132)
    } else {
        Color.LightGray
    }
}

@Composable
fun GoHome(vm:GameViewModel,navigate:()->Unit){
    Button(
        onClick = {
            navigate.invoke()
            vm.resetGame()
        }
    ){
        Text(
            modifier = Modifier.padding(2.dp),
            text = "Go Home".uppercase(),
            color = Color.Black,
        )
    }
}

@Composable
fun StartGame(vm:GameViewModel){
    Button(
        onClick = vm::startGame
    ){
        Text(
            modifier = Modifier.padding(2.dp),
            text = "Play Game".uppercase(),
            color = Color.Black,
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview(){
    GameScreen(FakeVM(),navigate = {"home"})
}
