package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(second, res[0])
        assertEquals(third, res[1])
        assertEquals(first, res[2])
    }

    @Test
    fun test_getLeaderboard_withRank_returnsRankAndNeighbors() {
        val p1 = GameResult(1, "p1", 100, 10.0)
        val p2 = GameResult(2, "p2", 90, 10.0)
        val p3 = GameResult(3, "p3", 80, 10.0)
        val p4 = GameResult(4, "p4", 70, 10.0)
        val p5 = GameResult(5, "p5", 60, 10.0)
        val p6 = GameResult(6, "p6", 50, 10.0)
        val p7 = GameResult(7, "p7", 40, 10.0)
        val p8 = GameResult(8, "p8", 30, 10.0)
        val p9 = GameResult(9, "p9", 20, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(p5, p2, p9, p1, p7, p3, p8, p6, p4))

        val res: List<GameResult> = controller.getLeaderboard(5)

        verify(mockedService).getGameResults()
        assertEquals(7, res.size)
        assertEquals(p2, res[0])
        assertEquals(p3, res[1])
        assertEquals(p4, res[2])
        assertEquals(p5, res[3])
        assertEquals(p6, res[4])
        assertEquals(p7, res[5])
        assertEquals(p8, res[6])
    }

    @Test
    fun test_getLeaderboard_withRankAtStart_returnsAvailableEntries() {
        val p1 = GameResult(1, "p1", 100, 10.0)
        val p2 = GameResult(2, "p2", 90, 10.0)
        val p3 = GameResult(3, "p3", 80, 10.0)
        val p4 = GameResult(4, "p4", 70, 10.0)
        val p5 = GameResult(5, "p5", 60, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(p5, p3, p1, p4, p2))

        val res: List<GameResult> = controller.getLeaderboard(1)

        verify(mockedService).getGameResults()
        assertEquals(4, res.size)
        assertEquals(p1, res[0])
        assertEquals(p2, res[1])
        assertEquals(p3, res[2])
        assertEquals(p4, res[3])
    }

    @Test
    fun test_getLeaderboard_withInvalidRankTooSmall_throwsBadRequest() {
        whenever(mockedService.getGameResults()).thenReturn(emptyList())

        val ex = assertThrows<ResponseStatusException> {
            controller.getLeaderboard(0)
        }

        verify(mockedService).getGameResults()
        assertEquals(HttpStatus.BAD_REQUEST, ex.statusCode)
    }

    @Test
    fun test_getLeaderboard_withInvalidRankTooLarge_throwsBadRequest() {
        val only = GameResult(1, "only", 10, 10.0)
        whenever(mockedService.getGameResults()).thenReturn(listOf(only))

        val ex = assertThrows<ResponseStatusException> {
            controller.getLeaderboard(2)
        }

        verify(mockedService).getGameResults()
        assertEquals(HttpStatus.BAD_REQUEST, ex.statusCode)
    }
}