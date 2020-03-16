package org.tron.pricecenter.utils.cryto;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.spongycastle.util.encoders.Hex;
import org.tron.common.utils.DataWord;

public class TRONAddressUtils {
  public static String ALL_ZERO_BASE58_ADDR = "T9yD14Nj9j7xAB4dbGeiX9h8unkKHxuWwb";

  public static byte addressPreFixByte = (byte) 0x41;   //41 + address;

  public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];


  /**
   * get bytes data from hex string data.
   */
  public static byte[] fromHexString(String data) {
    if (data == null) {
      return EMPTY_BYTE_ARRAY;
    }
    if (data.startsWith("0x")) {
      data = data.substring(2);
    }
    if (data.length() % 2 != 0) {
      data = "0" + data;
    }
    return Hex.decode(data);
  }

  public static byte[] convertToTronAddress(String hexString) {
    if (hexString.length() > 40) {
      hexString = hexString.substring(hexString.length() - 40, hexString.length());

    }
    byte[] ret = fromHexString(hexString);
    return convertToTronAddress(ret);
  }


  public static byte[] convertToTronAddress(byte[] address) {
    if (address.length == 20) {
      byte[] newAddress = new byte[21];
      byte[] temp = new byte[]{addressPreFixByte};
      System.arraycopy(temp, 0, newAddress, 0, temp.length);
      System.arraycopy(address, 0, newAddress, temp.length, address.length);
      address = newAddress;
    }
    return address;
  }

  public static String encode58Check(byte[] input) {
    byte[] hash0 = Sha256Hash.hash(input);
    byte[] hash1 = Sha256Hash.hash(hash0);
    byte[] inputCheck = new byte[input.length + 4];
    System.arraycopy(input, 0, inputCheck, 0, input.length);
    System.arraycopy(hash1, 0, inputCheck, input.length, 4);
    return Base58.encode(inputCheck);
  }


  public static String encode58CheckWithoutPrefix(byte[] address) {
    return encode58Check(convertToTronAddress(address));
  }

  public static String encode58Check(String hexString) {
    return encode58Check(convertToTronAddress(hexString));
  }

  public static ArrayList<String> unpackAddressList(byte[] data) {
    int des = DataWord.getDataWord(data, 0).intValue() / DataWord.WORD_LENGTH;
    int length = DataWord.getDataWord(data, des).intValue();
    if (length == 0) {
      return Lists.newArrayList();
    }

    length += des++;
    ArrayList<String> addressList = new ArrayList<>();
    do {
      byte[] address = DataWord.getDataWord(data, des).getLast20Bytes();
      addressList.add(encode58CheckWithoutPrefix(address));
    } while (des++ < length);
    return addressList;
  }
}
