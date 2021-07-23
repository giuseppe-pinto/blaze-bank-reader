package com.giuseppe.pinto.blaze.bank.reader.adpaters

import com.giuseppe.pinto.blaze.bank.reader.domain.User
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.testcontainers.containers.MySQLContainer
import javax.sql.DataSource


class UserRepositoryMySqlTest {

    lateinit var mysql: MySQLContainer<Nothing>
    lateinit var dataSource: DataSource

    @BeforeEach
    internal fun setUp() {
        mysql = MySQLContainer<Nothing>("mysql:5.5.53")
        mysql.start()
        dataSource = DataSourceBuilder.create()
            .url(mysql.jdbcUrl)
            .username(mysql.username)
            .password(mysql.password)
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .build()
    }

    @AfterEach
    internal fun tearDown() {
        mysql.close()
    }

    @Test
    internal fun `get the user info`() {

        val txManager = DataSourceTransactionManager(dataSource)
        val jdbcTemplate = JdbcTemplate(dataSource)
        val transaction = txManager.getTransaction(DefaultTransactionDefinition())
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
        txManager.commit(transaction)

        val sut = UserRepositoryMySql(JdbcTemplate(dataSource))

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
