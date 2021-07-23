package com.giuseppe.pinto.blaze.bank.reader.adpaters

import com.giuseppe.pinto.blaze.bank.reader.domain.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UserRepositoryMySqlTest {

    @Test
    internal fun `get the user info`() {

        val dao : UserRepositoryDao = mock()

        whenever(dao.getUser(1L)).thenReturn(
            UserDto(1L,
                "giuseppe",
                "Pinto",
                "cicciopasticcio@gmail.com",
                "8883434567",
                "via da via anfossi 37",
                "Milan",
                "IT"))

        val sut = UserRepositoryMySql(dao)

        val expectedUser = User(
            1L,
            "giuseppe",
            "Pinto",
            "cicciopasticcio@gmail.com",
            "8883434567",
            "via da via anfossi 37",
            "Milan",
            "IT"
        )

        val actualUser = sut.getUserInfo(1L)

        assertEquals(expectedUser, actualUser)
    }
}
