package com.android04.godfisherman.presentation.home

import com.android04.godfisherman.common.Result
import com.android04.godfisherman.common.constant.FishRankingRequest
import com.android04.godfisherman.domain.HomeRepository
import com.android04.godfisherman.presentation.InstantTaskExecutorRule
import com.android04.godfisherman.presentation.MainCoroutineRule
import com.android04.godfisherman.presentation.rankingdetail.RankingData
import com.android04.godfisherman.utils.GPS_ERROR
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class HomeViewModelTest {

    @Mock
    lateinit var homeRepository: HomeRepository
    private lateinit var homeViewModel: HomeViewModel

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        homeViewModel =
            HomeViewModel(homeRepository, mainCoroutineRule.testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @DisplayName("랭킹 정보 받아오기 실패 테스트")
    @Test
    fun fetchRanking_fail() = mainCoroutineRule.runBlockingTest {
        // Given
        val isFresh = false
        val rankingType = FishRankingRequest.HOME

        // When
        `when`(homeRepository.fetchRankingList(rankingType, isFresh)).thenReturn(
            Result.Fail("실패")
        )
        homeViewModel.fetchRanking(isFresh)

        // Then
        homeViewModel.error.value?.let { result ->
            assertEquals("실패", result.peekContent())
        }
    }

    @ExperimentalCoroutinesApi
    @DisplayName("랭킹 정보 받아오기 성공 테스트")
    @Test
    fun fetchRanking_success() = mainCoroutineRule.runBlockingTest {
        // Given
        val isFresh = false
        val rankingType = FishRankingRequest.HOME

        // When
        `when`(homeRepository.fetchRankingList(rankingType, isFresh)).thenReturn(
            Result.Success(
                listOf(
                    RankingData.HomeRankingData("testUser", "testFish", 10.0),
                    RankingData.HomeRankingData("testUser", "testFish", 10.0)
                )
            )
        )

        pauseDispatcher()
        homeViewModel.fetchRanking(isFresh)
        assertEquals(true, homeViewModel.isRankLoading.value)
        resumeDispatcher()
        assertEquals(2, homeViewModel.rankList.value?.size ?: 0)
        assertEquals(false, homeViewModel.isRankLoading.value)
    }

    @ExperimentalCoroutinesApi
    @DisplayName("위치 정보 받아오기 실패 테스트")
    @Test
    fun loadLocation_fail() = mainCoroutineRule.runBlockingTest {
        // Given
        val errorMessage = GPS_ERROR

        // When
        `when`(homeRepository.updateAddress()).thenReturn(
            Result.Fail(errorMessage)
        )
        homeViewModel.loadLocation()

        assertEquals(errorMessage, homeViewModel.address.value)
    }

    @ExperimentalCoroutinesApi
    @DisplayName("위치 정보 받아오기 성공 테스트")
    @Test
    fun loadLocation_success() = mainCoroutineRule.runBlockingTest {
        // Given
        val address = "테스트시 테스트구 테스트동"

        // When
        `when`(homeRepository.updateAddress()).thenReturn(
            Result.Success(address)
        )
        homeViewModel.loadLocation()

        assertEquals(address, homeViewModel.address.value)
    }
}
