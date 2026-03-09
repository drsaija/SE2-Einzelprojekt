package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import kotlin.test.assertEquals

class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock(GameResultService::class.java)
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult_returnsCorrectResult() {
        val result = GameResult(1, "player1", 10, 5.0)

        `when`(mockedService.getGameResult(1)).thenReturn(result)

        val res = controller.getGameResult(1)

        verify(mockedService).getGameResult(1)
        assertEquals(result, res)
    }

    @Test
    fun test_getAllGameResults_returnsList() {
        val results = listOf(
            GameResult(1, "p1", 10, 5.0),
            GameResult(2, "p2", 20, 3.0)
        )

        `when`(mockedService.getGameResults()).thenReturn(results)

        val res = controller.getAllGameResults()

        verify(mockedService).getGameResults()
        assertEquals(results, res)
    }

    @Test
    fun test_addGameResult_callsService() {
        val result = GameResult(0, "player", 10, 5.0)

        controller.addGameResult(result)

        verify(mockedService).addGameResult(result)
    }

    @Test
    fun test_deleteGameResult_callsService() {
        controller.deleteGameResult(1)

        verify(mockedService).deleteGameResult(1)
    }
}