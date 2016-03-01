package br.com.gbvbahia.maker.factories.types.works;

import br.com.gbvbahia.i18n.I18N;
import br.com.gbvbahia.maker.factories.types.managers.Notification;
import br.com.gbvbahia.maker.factories.types.managers.NotifierTests;
import br.com.gbvbahia.maker.factories.types.works.commons.ValueSpecializedFactory;
import br.com.gbvbahia.maker.log.LogInfo;
import br.com.gbvbahia.maker.types.complex.MakeString;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * Cria uma String que passa no teste de validação de CPF, nove caracteres mais dois digitos
 * verificadores.<br>
 * Para que funcione deve ter o valor isCPF no arquivo make.properties
 *
 * @since v.1 09/06/2012
 * @author Guilherme
 */
public class MakeCpf implements ValueSpecializedFactory {

  /**
   * Como o propertie deve estár definido no valor: "isCPF".
   */
  public static final String KEY_PROPERTY = "isCPF";

  /**
   * Cannot be instantiated outside.
   */
  private MakeCpf() {}

  /**
   * Retorna uma string no formato de um CPF válido, em relação a validação do digito verificador.
   * Retorna nove caracteres mais dois digitos verificadores, totalizando onze caracteres. Não
   * retorna formatado, somente números, pontos e ífen são excluídos. <br>
   * ATENÇÃO: Criado para facilitar testes de desenvolvimento de software.
   *
   * @return String no formato de um CPF válido.
   */
  public static String getCpf() {
    String noveDigitos = MakeString.getString(9, MakeString.StringType.NUMBER);
    return noveDigitos + calcularDigitoVerificador(noveDigitos);
  }

  @Override
  public <T> void makeValue(Field field, T entity, String... testName)
      throws IllegalAccessException, IllegalArgumentException {
    field.set(entity, getCpf());
  }

  @Override
  public boolean workValue(String fieldName, String value) {
    LogInfo.logDebugInformation("MakeCPF", I18N.getMsg("workValueMake", value));
    if (KEY_PROPERTY.equals(StringUtils.trim(value))) {
      return true;
    }
    LogInfo.logDebugInformation("MakeCPF", I18N.getMsg("notIsWork", "CPF", value));
    return false;
  }

  /**
   * Irá avaliar se o tipo do Field é trabalhado pelo mesmo, aqui deve ser String.
   *
   * @param field Field a ter o valor definido.
   * @return True para String False para o resto.
   */
  @Override
  public <T> boolean isWorkWith(Field field, T entity) {
    return field.getType().equals(String.class);
  }

  /**
   * Observer to warn about the test stage.
   */
  @Override
  public void updateStage(Notification notification) {
    if (notification.isTestFinished()) {
      instance = null;
    }
  }

  /**
   * Com base no cpf passado é calculado o dígito verificador.
   *
   * @param cpf Somente números.
   * @return duas Strings numéricas referente ao dígito verificador.
   */
  private static String calcularDigitoVerificador(String cpf) {
    Integer[] valores = new Integer[cpf.length()];
    Integer[] cpfPrimeiroDigito = new Integer[cpf.length() + 1];
    String[] values = cpf.split("");
    for (int i = 1, j = 0; (i < values.length) && (j < values.length); i++, j++) {
      valores[j] = new Integer(values[i]);
    }
    int calculoBase = 10;
    int calculoBaseDv2 = 11;
    Integer dv1 = new Integer(0);
    Integer dv2 = new Integer(0);
    String digitoVerificador = "";
    for (int i = 0; i < valores.length; i++) {
      dv1 += valores[i] * calculoBase;
      calculoBase--;
    }
    if ((dv1 % 11) < 2) {
      dv1 = new Integer(0);
    } else {
      dv1 = new Integer(11 - (dv1 % 11));
    }
    System.arraycopy(valores, 0, cpfPrimeiroDigito, 0, valores.length);
    cpfPrimeiroDigito[cpfPrimeiroDigito.length - 1] = dv1;
    for (int i = 0; i < cpfPrimeiroDigito.length; i++) {
      dv2 += cpfPrimeiroDigito[i] * calculoBaseDv2;
      calculoBaseDv2--;
    }
    if ((dv2 % 11) < 2) {
      dv2 = new Integer(0);
    } else {
      dv2 = new Integer(11 - (dv2 % 11));
    }
    digitoVerificador = digitoVerificador + dv1 + dv2;
    return digitoVerificador;
  }

  // ==============
  // Static control
  // ==============
  private static ValueSpecializedFactory instance = null;

  /**
   * Get a instance for this class encapsulated by ValueSpecializedFactory.
   * 
   * @return a instance for MakeCpf class encapsulated by ValueSpecializedFactory.
   */
  public static synchronized ValueSpecializedFactory getInstance() {
    if (instance == null) {
      instance = new MakeCpf();
      NotifierTests.getNotifyer().addObserver(instance);
    }
    return instance;
  }
}