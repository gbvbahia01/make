package br.com.gbvbahia.maker.factories.types.works;

import br.com.gbvbahia.i18n.I18N;
import br.com.gbvbahia.maker.factories.types.managers.Notification;
import br.com.gbvbahia.maker.factories.types.managers.NotifierStage;
import br.com.gbvbahia.maker.factories.types.works.commons.ValueSpecializedFactory;
import br.com.gbvbahia.maker.log.LogInfo;
import br.com.gbvbahia.maker.types.complex.MakeString;
import br.com.gbvbahia.maker.types.primitives.MakeBoolean;
import br.com.gbvbahia.maker.types.primitives.numbers.MakeInteger;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * Retorna uma string no formato de um CNPJ valido, em relacao a validacao do digito verificador.
 * Retorna 12 caracteres mais dois digitos verificadores, totalizando quatorze caracteres. Nao
 * retorna formatado, somente numeros, pontos, ifen e barra sao excluidos.<br>
 * ATENCAO: Criado para facilitar testes de desenvolvimento de software. <br>
 * Para que funcione deve ter o valor isCNPJ no arquivo make.properties.<br>
 * <br>
 * <b>MakeCnpj</b><br>
 * tag: isCNPJ<br>
 * Example: isCNPJ<br>
 * This specialized factory work with a type of number that exist in Brazil.<br>
 * Works only with String.<br>
 * 
 * @since v.1 09/06/2012
 * @author Guilherme
 */
public class MakeCnpj implements ValueSpecializedFactory {

  /**
   * Key for this specialized factory.
   */
  public static final String KEY_PROPERTY = "isCNPJ";

  /**
   * Cannot be instantiated outside.
   */
  private MakeCnpj() {}

  @Override
  public boolean workValue(String fieldName, String value) {
    LogInfo.logDebugInformation("MakeCNPJ", I18N.getMsg("workValueMake", value));
    if (KEY_PROPERTY.equals(StringUtils.trim(value))) {
      return true;
    }
    LogInfo.logDebugInformation("MakeCNPJ", I18N.getMsg("notIsWork", "CNPJ", value));
    return false;
  }

  @Override
  public <T> boolean isWorkWith(final Field field, final T entity) {
    return field.getType().equals(String.class);
  }

  @Override
  public <T> void makeValue(Field field, final T entity, String... testName)
      throws IllegalAccessException, IllegalArgumentException {
    field.set(entity, getCnpj());
  }

  /**
   * Observer to warn about the test stage.
   */
  @Override
  public void updateStage(Notification notification) {
    if (notification.isCreationFinished()) {
      instance = null;
    }
  }

  /**
   * Gera um CNPJ aleatorio mas valido, ou seja, o digito verificador correto.
   *
   * @return CNPJ valido.
   */
  public static String getCnpj() {
    String cnpj = MakeString.getString(8, MakeString.StringType.NUMBER);
    if (MakeBoolean.getBoolean()) {
      Integer filiais = MakeInteger.getMax(11);
      if (filiais > 9) {
        cnpj += "00" + MakeInteger.getRange(10, 99);
      } else if (filiais > 5) {
        cnpj += "000" + MakeInteger.getMax(9);
      } else {
        cnpj += "000" + MakeInteger.getMax(5);
      }
    } else {
      cnpj += "0001";
    }
    return cnpj + digitCalculate(cnpj);
  }

  /**
   * Cria os digitos verificadores do CNPJ com base nos 12 caracteres passados.
   *
   * @param strCnpj 12 caractres numericos do CNPJ
   * @return String no formato de CNPJ validas.
   */
  private static String digitCalculate(final String strCnpj) {
    final int four = 4;
    final int five = 5;
    final int six = 6;
    final int seven = 7;
    final int eight = 8;
    final int nine = 9;
    final int ten = 10;
    final int cpfCaracteres = 11;
    final int twelve = 12;
    final int fourEight = 48;
    char[] chrCnpj = new char[strCnpj.length() + 2];
    int soma = 0, dig;
    String cnpjCalc = strCnpj.substring(0, twelve);
    System.arraycopy(strCnpj.toCharArray(), 0, chrCnpj, 0, strCnpj.toCharArray().length);
    /*
     * Primeira parte
     */
    for (int i = 0; i < four; i++) {
      if (((chrCnpj[i] - fourEight) >= 0) && ((chrCnpj[i] - fourEight) <= nine)) {
        soma += (chrCnpj[i] - fourEight) * (six - (i + 1));
      }
    }
    for (int i = 0; i < eight; i++) {
      if (((chrCnpj[i + four] - fourEight) >= 0) && ((chrCnpj[i + four] - fourEight) <= nine)) {
        soma += (chrCnpj[i + four] - fourEight) * (ten - (i + 1));
      }
    }
    dig = cpfCaracteres - (soma % cpfCaracteres);
    cnpjCalc += ((dig == ten) || (dig == cpfCaracteres)) ? "0" : Integer.toString(dig);
    /*
     * Segunda parte
     */
    chrCnpj[twelve] = cnpjCalc.charAt(cnpjCalc.length() - 1);
    soma = 0;
    for (int i = 0; i < five; i++) {
      if (((chrCnpj[i] - fourEight) >= 0) && ((chrCnpj[i] - fourEight) <= nine)) {
        soma += (chrCnpj[i] - fourEight) * (seven - (i + 1));
      }
    }
    for (int i = 0; i < eight; i++) {
      if (((chrCnpj[i + five] - fourEight) >= 0) && ((chrCnpj[i + five] - fourEight) <= nine)) {
        soma += (chrCnpj[i + five] - fourEight) * (ten - (i + 1));
      }
    }
    dig = cpfCaracteres - (soma % cpfCaracteres);
    cnpjCalc += ((dig == ten) || (dig == cpfCaracteres)) ? "0" : Integer.toString(dig);
    return StringUtils.substring(cnpjCalc, twelve);
  }

  // ==============
  // Static control
  // ==============
  private static ValueSpecializedFactory instance = null;

  /**
   * Get a instance for this class encapsulated by ValueSpecializedFactory.
   * 
   * @return a instance of MakeCnpj encapsulated in ValueFactory.
   */
  public static synchronized ValueSpecializedFactory getInstance() {
    if (instance == null) {
      instance = new MakeCnpj();
      NotifierStage.getNotifyer().addObserver(instance);
    }
    return instance;
  }
}
