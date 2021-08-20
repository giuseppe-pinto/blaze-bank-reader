package com.giuseppe.pinto.blaze.bank.reader.adpaters.domain

import com.giuseppe.pinto.blaze.bank.reader.adpaters.domain.TransactionType.WITHDRAWAL
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.testcontainers.containers.MySQLContainer
import java.math.BigDecimal
import javax.sql.DataSource

class TransactionRepositoryMySqlTest {

    @Test
    fun `get a transaction by id`() {
        val userId = 1L
        val transactionId = 150L

        val sut = TransactionRepositoryMySql(jdbcTemplate)

        val expectedTransaction = PresentTransaction(
            transactionId,
            WITHDRAWAL,
            BigDecimal("50.00"),
            "WITHDRAWAL FOR RENT",
            userId
        )

        val actualTransaction = sut.getTransactionBy(transactionId)

        assertEquals(expectedTransaction, actualTransaction)
    }

    @Test
    fun `transaction not present`() {

        val notPresentTransactionId = 0L
        val expectedTransaction = NotPresentTransaction

        val sut = TransactionRepositoryMySql(jdbcTemplate)
        val actualTransaction = sut.getTransactionBy(notPresentTransactionId)
        assertEquals(expectedTransaction, actualTransaction)
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

            jdbcTemplate.execute(
                        "CREATE TABLE `TRANSACTION_TYPE` (" +
                        "  `TYPE` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL," +
                        "  `DESCRIPTION` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL," +
                        "  PRIMARY KEY (`TYPE`)" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"
            )
            jdbcTemplate.execute(
                "INSERT INTO TRANSACTION_TYPE (`TYPE`, DESCRIPTION) VALUES" +
                        "('WITHDRAWAL', 'WITHDRAWAL DESCRIPTION');"
            )

            jdbcTemplate.execute(
                "CREATE TABLE `TRANSACTION` (" +
                        "  `ID` bigint NOT NULL AUTO_INCREMENT," +
                        "  `TYPE` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL," +
                        "  `AMOUNT` decimal(15,2) NOT NULL," +
                        "  `DESCRIPTION` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL," +
                        "  `USER_ID` bigint NOT NULL," +
                        "  PRIMARY KEY (`ID`)," +
                        "  UNIQUE KEY `TRANSACTION_UN` (`ID`,`USER_ID`)," +
                        "  KEY `TRANSACTION_FK` (`USER_ID`)," +
                        "  KEY `TRANSACTION_FK_1` (`TYPE`)," +
                        "  KEY `TRANSACTION_ID_IDX` (`ID`,`USER_ID`) USING BTREE," +
                        "  CONSTRAINT `TRANSACTION_FK` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`ID`)," +
                        "  CONSTRAINT `TRANSACTION_FK_1` FOREIGN KEY (`TYPE`) REFERENCES `TRANSACTION_TYPE` (`TYPE`) ON UPDATE CASCADE" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"
            )
            jdbcTemplate.execute(
                "INSERT INTO `TRANSACTION` ( ID,`TYPE`, AMOUNT, DESCRIPTION, USER_ID) " +
                        "VALUES(150, 'WITHDRAWAL', 50.00, 'WITHDRAWAL FOR RENT', 1);"
            )

            txManager.commit(transaction)

            return jdbcTemplate

        }
    }


}