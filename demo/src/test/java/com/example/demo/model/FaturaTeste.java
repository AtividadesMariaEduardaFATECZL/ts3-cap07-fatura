package com.example.demo.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;


public class FaturaTeste {

    @Nested
    class CnpjIsValido {

        @ParameterizedTest
        @CsvSource(value = {
                "56710184000138",
                "87672124000100",
                "87672124000100"
        })
        void deve_retornar_verdadeiro_para_cnpj_com_formatos_corretos(String cnpj) {
            Fatura fatura = new Fatura();

            assertTrue(fatura.cnpjIsValido(cnpj));
        }

        @ParameterizedTest
        @CsvSource(value = {
                "00000000000000",
                "11111111111111",
                "33333333333333",
                "44444444444444",
                "55555555555555",
                "66666666666666",
                "77777777777777",
                "88888888888888",
                "99999999999999",
                "1247290237"

        })
        void deve_retornar_falso_para_cnpj_com_formatos_incorretos(String cnpj) {
            Fatura fatura = new Fatura();

            assertFalse(fatura.cnpjIsValido(cnpj));
        }
    }

    @Nested
    class SetDataVencimento {

        @ParameterizedTest
        @NullAndEmptySource
        void deve_jogar_exception_quando_a_data_de_vencimento_for_nula_e_vazia(String dataDeVencimento) {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> new Fatura().setDataVencimento(dataDeVencimento))
                    .withMessage("Data invalida");
        }

        @ParameterizedTest
        @CsvSource(value = {
                "1298312830"
        })
        @DisplayName("deve retornar excecao quando a data de vencimento nao esta no formato dd/MM/yyyy")
        void deve_retornar_excecao_quando_a_data_de_vencimento_nao_esta_no_formato_correto(String data) {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> new Fatura().setDataVencimento(data))
                    .withMessage("Data invalida");
        }
    }

    @Nested
    class isValida {
        @ParameterizedTest
        @CsvSource(value = {
                "1298312830",
                "2020-09-10",
                "2020/09/10"
        })
        @DisplayName("deve retornar falso quando a data de vencimento nao esta no formato dd/MM/yyyy")
        void deve_retornar_falso_quando_a_data_nao_esta_no_formato_correto(String data) {
            Fatura fatura = new Fatura();
            assertFalse(fatura.isValida(data));
        }

        @ParameterizedTest
        @CsvSource(value = {
                "10/09/2022",
                "19/10/2017",
                "25/1/2021"
        })
        @DisplayName("deve retornar falso quando a data de vencimento nao esta no formato dd/MM/yyyy")
        void deve_retornar_verdadeiro_quando_a_data_esta_no_formato_correto(String data) {
            Fatura fatura = new Fatura();
            assertTrue(fatura.isValida(data));
        }
    }
}
