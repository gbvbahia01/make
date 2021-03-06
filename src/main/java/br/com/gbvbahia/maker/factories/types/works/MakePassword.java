package br.com.gbvbahia.maker.factories.types.works;

import br.com.gbvbahia.i18n.I18N;
import br.com.gbvbahia.maker.types.primitives.numbers.MakeInteger;

/**
 * Cria uma senha, uma String contendo letras, números e caracteres especiais,{ "!", "#", "@", "&",
 * "$",}.
 *
 * @since v.1 09/06/2012
 * @author Guilherme
 */
public class MakePassword {

  /**
   * Se a solicitação de criação de Password for maior que este valor uma IllergalArgumentException
   * será lançada.
   */
  public static final int MAX_LENGTH_SUPPORTS = 100;

  /**
   * Gera uma senha com a quantidade de caracteres limitado ao solicitado.
   *
   * @param min Quantidade minima de caracteres solicitado.
   * @param max Quantidade maxima de caracteres solicitado. Esse valor é limitado a 100 caractres.
   * @return Senha com numeros, letras e caracteres especiais <code>(!,#,@,&,$)</code>
   */
  public static String getPassword(final int min, final int max) {
    if (min < 0) {
      throw new IllegalArgumentException(I18N.getMsg("caractereToStringErro", new Integer(min)));
    }
    int numero = MakeInteger.getRange(min, max);
    return getPassword(numero);
  }

  /**
   * Gera uma senha com a quantidade de caracteres solicitado.
   *
   * @param caracteres Quantidade de caracteres solicitado. Esse valor é limitado a 100 caractres.
   * @return Senha com numeros, letras e caracteres especiais <code>(!,#,@,&,$)</code>
   */
  public static String getPassword(final int caracteres) {
    if (caracteres > MAX_LENGTH_SUPPORTS) {
      throw new IllegalArgumentException(I18N.getMsg("passWordsizeLenghFatal", MAX_LENGTH_SUPPORTS));
    } else if (caracteres < 0) {
      throw new IllegalArgumentException(I18N.getMsg("caractereToStringErro", new Integer(
          caracteres)));
    }
    String toReturn = "";
    for (int i = 0; i < caracteres; i++) {
      String[] characteres =
          {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "!", "#", "@", "&", "$", "A", "a",
              "B", "b", "C", "c", "D", "d", "E", "e", "F", "f", "G", "g", "H", "h", "I", "i", "J",
              "j", "K", "k", "L", "l", "Z", "z", "!", "#", "@", "&", "$", "1", "2", "3", "4", "5",
              "6", "7", "8", "9", "0", "K", "k", "L", "l", "M", "m", "N", "n", "O", "o", "P", "p",
              "Q", "q", "R", "r", "S", "s", "T", "t", "!", "#", "@", "&", "$", "1", "2", "3", "4",
              "5", "6", "7", "8", "9", "0", "U", "u", "V", "v", "X", "x", "Z", "z"};
      int letra1 = MakeInteger.getMax(characteres.length - 1);
      toReturn += characteres[letra1];
    }
    return toReturn;
  }
}
