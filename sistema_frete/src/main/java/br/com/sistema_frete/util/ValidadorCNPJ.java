package br.com.sistema_frete.util;

public final class ValidadorCNPJ {

    private ValidadorCNPJ() {
    }

    public static boolean isValid(String cnpj) {
        if (cnpj == null) {
            return false;
        }

        String numero = cnpj.replaceAll("\\D", "");

        if (numero.length() != 14 || numero.matches("(\\d)\\1{13}")) {
            return false;
        }

        int digito1 = calcularDigito(numero.substring(0, 12));
        int digito2 = calcularDigito(numero.substring(0, 12) + digito1);

        return numero.equals(numero.substring(0, 12) + digito1 + digito2);
    }

    private static int calcularDigito(String base) {
        int[] pesos;
        if (base.length() == 12) {
            pesos = new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        } else {
            pesos = new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        }

        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            soma += Character.getNumericValue(base.charAt(i)) * pesos[i];
        }

        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }
}