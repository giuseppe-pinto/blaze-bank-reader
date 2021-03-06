package com.giuseppe.pinto.blaze.bank.reader.adapters.repository

import com.giuseppe.pinto.blaze.bank.reader.domain.model.NotPresentUser
import com.giuseppe.pinto.blaze.bank.reader.domain.model.PresentUser
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.testcontainers.containers.MySQLContainer
import javax.sql.DataSource


class UserRepositoryMySqlTest {

    @Test
    fun `get the user info`() {

        val sut = UserRepositoryMySql(jdbcTemplate)

        val expectedUser = PresentUser(
            1L,
            "giuseppe",
            "Pinto",
            "cicciopasticcio@gmail.com",
            "8883434567",
            "via da via anfossi 37",
            "Milan",
            "IT"
        )

        val actualUser = sut.getUserInfoBy(1L)

        assertEquals(expectedUser, actualUser)
    }

    @Test
    fun `user not present`() {

        val idNotPresent = 123L
        val sut = UserRepositoryMySql(jdbcTemplate)
        val actualUser = sut.getUserInfoBy(idNotPresent)
        assertEquals(NotPresentUser, actualUser)
    }

    companion object {
        lateinit var mysql: MySQLContainer<Nothing>
        lateinit var dataSource: DataSource
        lateinit var jdbcTemplate: JdbcTemplate

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            mysql = MySQLContainer<Nothing>("mysql:8.0.26")
            mysql.start()
            dataSource = DataSourceBuilder.create()
                .url(mysql.jdbcUrl)
                .username(mysql.username)
                .password(mysql.password)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build()

            jdbcTemplate = createAndInitializeDatabase()
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            mysql.close()
        }

        private fun createAndInitializeDatabase(): JdbcTemplate {
            val txManager = DataSourceTransactionManager(dataSource)
            val transaction = txManager.getTransaction(DefaultTransactionDefinition())
            val jdbcTemplate = JdbcTemplate(dataSource)
            jdbcTemplate.execute(
                "" +
                        "CREATE TABLE `USER` (" +
                        "  `ID` bigint NOT NULL AUTO_INCREMENT," +
                        "  `NAME` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL," +
                        "  `LASTNAME` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL," +
                        "  `EMAIL` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL," +
                        "  `PHONE` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL," +
                        "  `ADDRESS` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL," +
                        "  `COUNTRY` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL," +
                        "  `CITY` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL," +
                        "  PRIMARY KEY (`ID`)," +
                        "  UNIQUE KEY `USER_UN` (`NAME`,`LASTNAME`,`EMAIL`)," +
                        "  KEY `USER_ID_IDX` (`ID`) USING BTREE" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"
            )
            jdbcTemplate.execute("INSERT INTO `USER`" +
                    "(ID, NAME, LASTNAME, EMAIL, PHONE, ADDRESS, COUNTRY, CITY)" +
                    "VALUES(1, 'giuseppe', 'Pinto', 'cicciopasticcio@gmail.com', '8883434567', 'via da via anfossi 37', 'IT', 'Milan');")

            jdbcTemplate.execute("INSERT INTO `USER`" +
                    "(ID, NAME, LASTNAME, EMAIL, PHONE, ADDRESS, COUNTRY, CITY)" +
                    "VALUES(2, 'dad', 'safas', 'sdas@gmail.com', '8883434567', 'via da via anfossi 37', 'IT', 'Milan');")

            txManager.commit(transaction)

            return jdbcTemplate

        }
    }


}
