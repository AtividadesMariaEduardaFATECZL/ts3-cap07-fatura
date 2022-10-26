package com.example.demo.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

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
                "30/10/2022"
        })
        void deve_jogar_exception_quando_a_data_nao_for_em_um_dia_de_dia_domingo(String dataDeVencimento) {
            Fatura fatura = new Fatura();

            fatura.setDataEmissao("20/10/2022");

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> fatura.setDataVencimento(dataDeVencimento))
                    .withMessage("Data invalida");
        }

        @ParameterizedTest
        @CsvSource(value = {
                "19/10/2022"
        })
        void deve_jogar_exception_quando_a_data__de_emissao_for_depois_da_data_de_vencimento(String dataDeVencimento) {
            Fatura fatura = new Fatura();

            fatura.setDataEmissao("21/10/2022");

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> fatura.setDataVencimento(dataDeVencimento))
                    .withMessage("Data invalida");
        }

        @ParameterizedTest
        @CsvSource(value = {
                "19/10/2022"
        })
        @DisplayName("deve retornar a data de vencimento quando for uma data com o formato válido, maior do que a data de emissao e não seja domingo")
        void deve_retornar_a_data_de_vencimento_quando_for_uma_data_valida(String dataDeVencimento) {
            Fatura fatura = new Fatura();

            fatura.setDataEmissao("18/10/2022");

            assertEquals(fatura.setDataVencimento(dataDeVencimento), dataDeVencimento);
        }


        @ParameterizedTest
        @CsvSource(value = {
                "1298312830"
        })
        @DisplayName("deve retornar excecao quando a data de vencimento nao esta no formato dd/MM/yyyy")
        void deve_retornar_excecao_quando_a_data_nao_esta_no_formato_correto(String data) {
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

    @Nested
    class DtVencMaiorDtAtual {
        @ParameterizedTest
        @CsvSource(value = {
                "10/09/2022, 10/09/2023",
                "10/09/2021, 10/09/2022",
                "09/09/2000, 10/09/2001",
                "10/09/2002, 10/09/2003"
        })
        void deve_retornar_verdadeiro_quando_a_data_atual_e_maior_do_que_a_data_de_vencimento(String dataAtual, String dataVencimento) {
            Fatura fatura = new Fatura();
            assertTrue(fatura.dtVencMaiorDtAtual(dataAtual, dataVencimento));
        }

        @ParameterizedTest
        @CsvSource(value = {
                "10/09/2023, 10/09/2022",
                "10/09/2022, 10/09/2021",
                "09/09/2001, 10/09/2000",
                "10/09/2003, 10/09/2002"
        })
        void deve_retornar_falso_quando_data_de_vencimento_e_maior_do_que_data_atual(String dataAtual, String dataVencimento) {
            Fatura fatura = new Fatura();

            assertFalse(fatura.dtVencMaiorDtAtual(dataAtual, dataVencimento));
        }
    }

    @Nested
    class EhDomingo {
        @ParameterizedTest
        @CsvSource(value = {
                "11/09/2022",
                "18/09/2022",
                "25/09/2022",
                "02/10/2022",
        })
        void deve_retornar_verdadeiro_para_datas_que_caem_no_domingo(String data) {
            Fatura fatura = new Fatura();

            assertTrue(fatura.ehDomingo(data));
        }

        @ParameterizedTest
        @CsvSource(value = {
                "10/09/2022",
                "17/09/2022",
                "24/09/2022",
                "01/10/2022",
        })
        void deve_retornar_falso_para_dias_que_nao_sao_domingo(String data) {
            Fatura fatura = new Fatura();

            assertFalse(fatura.ehDomingo(data));
        }
    }

    @Nested
    class SetServicoContratado {
        @ParameterizedTest
        @NullAndEmptySource
        void deve_jogar_exception_quando_o_servico_for_nulo_ou_vazio(String servico) {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> new Fatura().setServicoContratado(servico))
                    .withMessage("Descriminacao do servico invalido");
        }

        @ParameterizedTest
        @CsvSource(value = {
                "Comércio",
                "Lazer e saúde"
        })
        void deve_retornar_o_servico_quando_nao_for_nulo_ou_vazio(String servico) {
            Fatura fatura = new Fatura();

            assertEquals(fatura.setServicoContratado(servico), servico);
        }
    }

    @Nested
    class SetValorFatura {
        @ParameterizedTest
        @CsvSource(value = {
                "1",
                "2",
                "3",
        })
        void deve_retornar_o_valor_da_fatura_caso_seja_maior_do_que_zero(String v) {
            Fatura fatura = new Fatura();

            assertEquals(fatura.setValorFatura(v), Float.parseFloat(v));
        }

        @ParameterizedTest
        @CsvSource(value = {
                "0",
                "-1"
        })
        void deve_jogar_exception_quando_o_valor_da_fatura_for_menor_do_que_zero(String v) {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> new Fatura().setValorFatura(v))
                    .withMessage("Valor invalido");
        }
    }

    @Nested
    class SetCnpj {
        @ParameterizedTest
        @CsvSource(value = {
                "56710184000138",
                "87672124000100",
                "87672124000100"
        })
        @DisplayName("deve retornar o cnpj se nao for nulo")
        void deve_retornar_o_cnpj_se_for_valido(String cnpj) {
            Fatura fatura = new Fatura();

            assertEquals(fatura.setCnpj(cnpj), cnpj);
        }

        @ParameterizedTest
        @NullSource
        void deve_jogar_exception_quando_o_cnpj_for_nulo(String cnpj) {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> new Fatura().setCnpj(cnpj))
                    .withMessage("CNPJ invalido");
        }
    }
}
