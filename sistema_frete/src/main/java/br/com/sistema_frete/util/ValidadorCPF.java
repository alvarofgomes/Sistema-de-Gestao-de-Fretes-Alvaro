package br.com.sistema_frete.util;

public final class ValidadorCPF {

    private ValidadorCPF() {
    }

    public static boolean isValid(String cpf) {
        if (cpf == null) {
            return false;
        }

        String numero = cpf.replaceAll("\\D", "");

        if (numero.length() != 11 || numero.matches("(\\d)\\1{10}")) {
            return false;
        }

        int digito1 = calcularDigito(numero.substring(0, 9), 10);
        int digito2 = calcularDigito(numero.substring(0, 9) + digito1, 11);

        return numero.equals(numero.substring(0, 9) + digito1 + digito2);
    }

    private static int calcularDigito(String base, int pesoInicial) {
        int soma = 0;
        int peso = pesoInicial;

        for (int i = 0; i < base.length(); i++) {
            soma += Character.getNumericValue(base.charAt(i)) * peso--;
        }

        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }
}