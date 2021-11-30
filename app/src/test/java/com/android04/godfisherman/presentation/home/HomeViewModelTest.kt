package com.android04.godfisherman.presentation.home

import com.android04.godfisherman.common.Result
import com.android04.godfisherman.common.constant.FishRankingRequest
import com.android04.godfisherman.data.repository.HomeRepository
import com.android04.godfisherman.data.repository.LocationRepository
import com.android04.godfisherman.data.repository.LogInRepository

import com.android04.godfisherman.presentation.InstantTaskExecutorRule
import com.android04.godfisherman.presentation.MainCoroutineRule
import com.android04.godfisherman.presentation.rankingdetail.RankingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class HomeViewModelTest {

    @Mock lateinit var homeRepository: HomeRepository
    @Mock private lateinit var loginRepository: LogInRepository
    @Mock private lateinit var locationRepository: LocationRepository
    private lateinit var homeViewModel: HomeViewModel

    @get:Rule val instantExecutor = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule val mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        homeViewModel =
            HomeViewModel(homeRepository, loginRepository, locationRepository, mainCoroutineRule.testDispatcher)
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
}
