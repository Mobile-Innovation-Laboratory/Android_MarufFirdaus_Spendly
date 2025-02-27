package dev.maruffirdaus.spendly.data.repository

import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.data.local.dao.ActivityDao
import dev.maruffirdaus.spendly.data.local.model.ActivityEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ActivityRepositoryImplTest {
    @Mock
    private lateinit var dao: ActivityDao
    private lateinit var repository: ActivityRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = ActivityRepositoryImpl(dao)
    }

    @Test
    fun `getActivities returns empty list when no matching items in database`() = runTest {
        val mockActivities = emptyList<ActivityEntity>()

        val walletId = "1"
        val categoryId = "1"
        val year = "2024"
        val month = "04"
        val date = buildString {
            append(year)
            append("-")
            append(month)
            append("%")
        }

        `when`(dao.getActivities(walletId, categoryId, date)).thenReturn(mockActivities)

        val result = repository.getActivities(walletId, categoryId, year, month)
        val expected = emptyList<Activity>()

        assertEquals(result, expected)
    }
}