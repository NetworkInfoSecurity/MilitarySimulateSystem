package thu.infosecurity.simulate.util.BaseElement;

import thu.infosecurity.simulate.util.NumberTheory.BigNumGCD;
import java.math.BigInteger;
import java.util.Random;

/**
 * Created by ForestNeo on 2016/10/8.
 * Class for bigNumber
 * Not support for number less than ZERO
 * 12345 is initialized by string"12345", and BigNum = "54321", bigNum[0] = '5', bigNum[1] = '4' etc.
 */
public class BigNum {
    private char[] bn;/*倒序存放*/

    public static BigNum BigZero = new BigNum(0);
    public static BigNum BigOne = new BigNum(1);
    public static BigNum BigTwo = new BigNum(2);

    public static void main(String[] args) throws Exception{

//        BigNum bn2 = new BigNum("3");
//        BigNum bn1 = new BigNum("2");
//        System.out.println("bn1 = " + bn1.toString());
//        System.out.println("bn2 = " + bn2.toString());
//        /*加法测试*/
//        BigNum bnsum = bn1.bigNumAdd(bn2);
//        System.out.println("bn1 + bn2 = " + bnsum);
//        /*减法测试*/
//        BigNum bnsub = bn1.bigNumSub(bn2);
//        System.out.println("bn1 - bn2 = " + bnsub);
//        /*乘法测试*
//        BigNum bnmul = bn1.bigNumMul(bn2);
//        System.out.println("bn1 * bn2 = " + bnmul);
        /*除法测试*/
//        BigNum bndiv = bn1.bigNumDiv(bn2);
//        System.out.println("bn1 / bn2 = " + bndiv);
//        /*取模测试*/
//        BigNum bnmod = bn1.bigNumMod(bn2);
//        System.out.println("bn1 % bn2 = " + bnmod);
        /*奇偶判断测试*/

//        System.out.println("bn1 是奇数: " + bn1.isOdd());
//        System.out.println("bn1 是偶数: " + bn1.isEven());
        BigNum bnUpper = new BigNum("179769313486231590772930519078902473361797697894230657273430081157732675805500963132708477322407536021120113879871393357658789768814416622492847430639474124377767893424865485276302219601246094119453082952085005768838150682342462881473913110540827237163350510684586298239947245938479716304835356329624224137215");

        /*次方求模测试*/
        BigNum bnBase = new BigNum("64363453465346543763457543634564354634325325476356354353526547356329857329849274927594835846509824759843554653476547634564236345735647563765464365245367234634534653465437634575436345643546343253254763563543535265473563634534653465437634575436345643546343253254763563543535265473563634534653465437634575436345643546343253254763563543535265473563566");
        BigNum bnTimes = new BigNum("2016312938547644363475648725436423543565473573456635724562465569042509437509247650894276819345723453456456357564845673546546734573578354436462346523465437234534564563575648456735465467345735723453456456357564845673546546734573556452436723453456456357564845673546546734573572345345645635756484567354654673457357234534564563575648456735465467345735");
        BigNum bnMod = new BigNum("459047092357230947503476092543769243645457357534645353242342353426495794357029487598437524254624624345635473572623452348678924365892346523464352434563547357262345234867892436589234652346435243456354735726234523486789243658923465234643524345635473572623452348678924365892346523464352434563547357262345234867892436589234652346435");

        BigInteger bnBaseInt = new BigInteger("64363453465346543763457543634564354634325325476356354353526547356329857329849274927594835846509824759843554653476547634564236345735647563765464365245367234634534653465437634575436345643546343253254763563543535265473563634534653465437634575436345643546343253254763563543535265473563634534653465437634575436345643546343253254763563543535265473563566");
        BigInteger bnTimesInt = new BigInteger("2016312938547644363475648725436423543565473573456635724562465569042509437509247650894276819345723453456456357564845673546546734573578354436462346523465437234534564563575648456735465467345735723453456456357564845673546546734573556452436723453456456357564845673546546734573572345345645635756484567354654673457357234534564563575648456735465467345735");
        BigInteger bnModInt = new BigInteger("459047092357230947503476092543769243645457357534645353242342353426495794357029487598437524254624624345635473572623452348678924365892346523464352434563547357262345234867892436589234652346435243456354735726234523486789243658923465234643524345635473572623452348678924365892346523464352434563547357262345234867892436589234652346435");

        long timeStart3 = System.currentTimeMillis();
        BigInteger mulInt = bnBaseInt.multiply(bnTimesInt);
        long timeEnd3 = System.currentTimeMillis();
        System.out.println("Time3 used : " + (timeEnd3 - timeStart3) + "mm");


        long timeStart1 = System.currentTimeMillis();
        BigNum mul = bnBase.bigNumMul(bnTimes);
        long timeEnd1 = System.currentTimeMillis();
        System.out.println("Time1 used : " + (timeEnd1 - timeStart1) + "mm");

        long timeStart2 = System.currentTimeMillis();
        BigNum bnModAnsFast = bnBase.bigNumPowerModFast(bnTimes, bnMod);
        long timeEnd2 = System.currentTimeMillis();
        System.out.println("Time2 used : " + (timeEnd2 - timeStart2) + "mm");
        System.out.println("FAST   :: bnBase ^ bnTimes % bnMod = " + bnModAnsFast);


        /*次方测试*/
        BigNum base = new BigNum("2");
        BigNum times = new BigNum("1024");
        System.out.println(base.bigNumPower(times));

    }

    /*Initial as '0' is input is null*/
    public BigNum(){
        bn = new char[1];
        bn[0] = '0';
    }
    public BigNum(Integer num){
        String bigNumStr = num.toString();
        bn = new char[bigNumStr.length()];
        for(int i = 0; i < bigNumStr.length(); i++)
            bn[i] = bigNumStr.charAt(bigNumStr.length()-1-i);
    }
    public BigNum(String bigNumberStr) throws Exception{
        for(char ch : bigNumberStr.toCharArray()){
            if (!(ch >= '0' && ch <= '9')) {
                System.err.println("Err in BigNum(), please use legal string to initialize a BigNum");
                throw new Exception("Please use legal string to initialize a bigNumber");
            }
        }
        bn = new char[bigNumberStr.length()];
        for(int i = 0; i < bigNumberStr.length(); i++)
            bn[i] = bigNumberStr.charAt(bigNumberStr.length()-1-i);
    }

    /*BigNum that is safe, only used private*/
    public BigNum(char[] charArray){
        if(charArray == null){
            this.bn = new char[1];
            bn[0] = '0';
        }
        else
            this.bn = removePrefixZero(charArray);
    }

    /*不知道什么类*/
    public int length(){
        return bn.length;
    }

    /*比较类*/
    public boolean isBiggerThan(BigNum cmpNum) throws Exception{
        return 1 == charArrayCmp(this.bn, cmpNum.getCharArrayFromBn());
    }
    public boolean isSmallerThan(BigNum cmpNum) throws Exception{
        return -1 == charArrayCmp(this.bn, cmpNum.getCharArrayFromBn());
    }
    public boolean isSame(BigNum cmpNum) throws Exception{
        return 0 == charArrayCmp(this.bn, cmpNum.getCharArrayFromBn());
    }
    /*判断奇数*/
    public boolean isOdd(){
        return (bn[0] - '0') % 2 == 1;
    }
    /*判断偶数*/
    public boolean isEven(){
        return !this.isOdd();
    }
    public boolean isZero() throws Exception{
        return this.isSame(new BigNum("0"));
    }

    /*操作类*/
    public BigNum bigNumAdd(BigNum addend) throws Exception{
        char[] retCharArray = charArrayAdd(charArrayCopy(this.bn), addend.getCharArrayFromBn());
        return new BigNum(retCharArray);
    }
    public BigNum bigNumSub(BigNum subend) throws Exception{
        if (this.isSmallerThan(subend)) {
            System.err.println("Err in bigNumSub()");
            System.out.println("in a-b: a = " + this);
            System.out.println("in a-b: b = " + subend);
            throw new Exception("减数太大了");
        }
        char[] ans = charArraySub(charArrayCopy(this.bn), subend.getCharArrayFromBn());
        return new BigNum(ans);
    }

    public BigNum bigNumMul(BigNum multiplicand) throws Exception {
        BigInteger bi = this.toBigInteger().multiply(multiplicand.toBigInteger());
        return BigIntegerToBigNum(bi);
    }
    public BigNum bigNumMul2(BigNum multiplicand) throws Exception{
        return new BigNum(charArrayMul(charArrayCopy(this.bn), multiplicand.getCharArrayFromBn()));
    }

    public BigNum bigNumDiv(BigNum divend) throws Exception{
        BigInteger bi = this.toBigInteger().divide(divend.toBigInteger());
        return BigIntegerToBigNum(bi);
    }

    public BigNum bigNumDiv2(BigNum divend) throws Exception{
        return new BigNum((charArrayDiv(charArrayCopy(this.bn), divend.getCharArrayFromBn())));
    }

    /*this ^ power*/
    public BigNum bigNumPower(BigNum power) throws Exception{
        if (power.isSame(new BigNum("0")))
            return new BigNum("1");
        if (power.isSame(new BigNum("1")))
            return new BigNum(this.getCharArrayFromBn());
        if (power.isSame(new BigNum("2")))
            return this.bigNumMul(this);

        if (power.isEven()) {//b偶数
            /*a ^ b  = [a ^ (b/2) ] ^ 2*/
            BigNum div = power.bigNumDiv(new BigNum("2"));
            return this.bigNumPower(div).bigNumPower(new BigNum("2"));
        }
        /*b奇数*/
        /*a ^ b % c = [a ^ (b/2) % c] ^ 2 % c + a % c*/
        BigNum bdiv = power.bigNumDiv(new BigNum("2"));
        return this.bigNumPower(bdiv).bigNumPower(new BigNum("2")).bigNumMul(this);
        //BigNum temp1 = this.bigNumPowerModFast(bdiv, mod).bigNumPowerModFast(new BigNum("2"), mod);
//        BigNum temp2 = this.bigNumMod(mod);
//        return temp1.bigNumMul(temp2).bigNumMod(mod);
    }
    public BigNum bigNumMod(BigNum mod) throws Exception{
        /* a % b = a - a /ｂ * b */
        BigNum step1 = this.bigNumDiv(mod);
        BigNum step2 = step1.bigNumMul(mod);
        BigNum ret = this.bigNumSub(step2);
        return ret;

    }

    /*计算this ^ power % mod*/
    public BigNum bigNumPowerMod(BigNum power, BigNum mod) throws Exception{
        if (power.isSame(new BigNum("0")))
            return new BigNum("1");
        if (power.isSame(new BigNum("1")))
            return this.bigNumMod(mod);

//        System.out.println("computing power mod");
        BigNum ret = this;
        BigNum powerTimes = new BigNum("1");
        ret = ret.bigNumMod(mod);

        while(powerTimes.isSmallerThan(power)){
//            System.out.println("PowerTimes = " + powerTimes);
            ret = ret.bigNumMul(this);
//            System.out.println("an1 = " + ret);
            ret = ret.bigNumMod(mod);
//            System.out.println("an1 = " + ret);
            powerTimes = powerTimes.bigNumAdd(new BigNum("1"));
//            System.out.println("an = " + ret);

        }
        return ret;
    }
    /*计算this ^ power % mod*/
    public BigNum bigNumPowerModFast(BigNum power, BigNum mod) throws Exception{
        if (power.isSame(new BigNum("0")))
            return new BigNum("1");
        if (power.isSame(new BigNum("1")))
            return this.bigNumMod(mod);
        if (power.isSame(new BigNum("2")))
            return this.bigNumPowerMod(new BigNum("2"), mod);

        if (power.isEven()) {//b偶数
            /*a ^ b % c = [a ^ (b/2) % c] ^ 2 % c*/
            BigNum div = power.bigNumDiv(new BigNum("2"));
            return this.bigNumPowerModFast(div, mod).bigNumPowerModFast(new BigNum("2"), mod);
        }
        /*b奇数*/
        /*a ^ b % c = [a ^ (b/2) % c] ^ 2 % c + a % c*/
        BigNum bdiv = power.bigNumDiv(new BigNum("2"));
        BigNum temp1 = this.bigNumPowerModFast(bdiv, mod).bigNumPowerModFast(new BigNum("2"), mod);
        BigNum temp2 = this.bigNumMod(mod);
        return temp1.bigNumMul(temp2).bigNumMod(mod);
        /**/
    }


    public String toString(){
        char[] bnTemp = new char[bn.length];
        for(int i = 0; i < bn.length; i++)
            bnTemp[i] = bn[bn.length - 1 - i];
        return(new String(bnTemp));
    }
    public BigInteger toBigInteger(){
        return new BigInteger(this.toString());
    }

    public BigNum BigIntegerToBigNum(BigInteger integerVal) throws Exception{
        return new BigNum(integerVal.toString());

    }


    public char[] getCharArrayFromBn() throws Exception{
        return charArrayCopy(this.bn);
    }

    /*中间过程函数*/
    private int max(int a, int b){
        return a > b ? a : b;
    }
    private int min(int a, int b){
        return a > b ? b : a;
    }
    private char[] charArrayAdd(char[] charListA, char[] charListB) {
        if(charListA.length < charListB.length){
            char[] t = charListA;
            charListA = charListB;
            charListB = t;
        }

        int length = max(charListA.length, charListB.length) + 1;
        char[] sumList = new char[length];

        /*charListA is longer that charListB*/
        int carry = 0;
        for(int i = 0; i < charListB.length; i++){
            int sum = (charListA[i] - '0') + (charListB[i] - '0') + carry;
            sumList[i] = (char)('0' + sum % 10);
            carry = sum / 10;
        }
        for(int i = charListB.length; i < charListA.length; i++){
            int sum = charListA[i] - '0' + carry;
            sumList[i] = (char)('0' + sum % 10);
            carry = sum / 10;
        }
        sumList[charListA.length] = (char)(carry + '0');
        return removePrefixZero(sumList);
    }
    private char[] charArraySub(char[] charListA, char[] charListB) {
        char[] bn1 = removePrefixZero(charListA);
        char[] bn2 = removePrefixZero(charListB);
        for(int i = 0; i < bn2.length; i++) {
            int tempVal = (bn1[i] - '0') - (bn2[i] - '0');
            if(tempVal < 0){
                tempVal = tempVal + 10;
                bn1[i] = (char)(tempVal + '0');
                /*向前借位*/
                int flag = i + 1;
                while(bn1[flag] == '0')
                    bn1[flag++] = '9';
                //System.out.println("flag = " + flag);
                bn1[flag] = (char)(bn1[flag] - 1);
            }
            else
                bn1[i] = (char)(tempVal + '0');
        }
        return removePrefixZero(bn1);
    }
    private char[] charArrayMul(char[] charListA, char[] charListB) {

        // 两数乘积位数不会超过乘数位数和+ 3位
        int csize = charListA.length + charListB.length + 3;
        // 开辟乘积数组
        int[] c = new int[csize];
        // 乘积数组填充0
        for (int i = 0; i < csize; i++)
            c[i] = 0;

        // 对齐逐位相乘
        for (int j = 0; j < charListB.length; j++)
            for (int i = 0; i < charListA.length; i++)
                c[i + j] +=  Integer.parseInt(String.valueOf(charListA[i]))* Integer.parseInt(String.valueOf(charListB[j]));

        for (int i = 0; i < csize-1; i++) {
            int carry = c[i] / 10;
            c[i] = c[i] % 10;
            c[i + 1] += carry;
        }

        char[] ret = new char[c.length];
        for(int i = 0; i < ret.length; i++)
            ret[i] = (char)(c[i] + '0');

        ret = removePrefixZero(ret);
        return ret;
    }
    private char[] charArrayDiv(char[] charListA, char[] charListB) throws Exception{
        char[] a = removePrefixZero(charListA);
        char[] b = removePrefixZero(charListB);
        char[] ans = removePrefixZero("0".toCharArray()); //存放商

        while (charArrayCmp(a, b) >= 0){
            /*a > b*/
            if (a.length == b.length) {
                a = charArraySub(a, b);
                ans = charArrayAdd(ans, "1".toCharArray());
            }
            else {
                char[] tempA = getFirstChar(a, b.length);/*取a的前面b位*/

                if(charArrayCmp(tempA, b) >= 0){
                    /*说明a > b * 10 ^ (a.length - b.length)*/
                    char[] bPow = charArrayPowersTen(b, a.length - b.length);
                    char[] temp = charArrayPowersTen("1".toCharArray(), a.length - b.length);
                    a = charArraySub(a, bPow);
                    ans = charArrayAdd(ans, temp);
                    // TODO: 2016/10/13 可以加速计算
                }
                else{
                    /*需要取a前面的b.length+1位，如65443 / 754需要取6544 / 754*/
                    char[] bPow = charArrayPowersTen(b, a.length - b.length - 1);
                    char[] temp = charArrayPowersTen("1".toCharArray(), a.length - b.length - 1);
                    a = charArraySub(a, bPow);
                    ans = charArrayAdd(ans, temp);
                }
            }
            a = removePrefixZero(a);
            ans = removePrefixZero(ans);
        }
        /*a < b说明a是余数*/
        return ans;

    }

    /*比较两个数的大小，1表示a>b,0表示a=b,-1表示a<b*/
    private int charArrayCmp(char[] charListA, char[] charListB){
        char[] a = removePrefixZero(charListA);
        char[] b = removePrefixZero(charListB);
        if (a.length > b.length)
            return 1;
        if (a.length < b.length)
            return -1;
        for(int i = a.length - 1; i >= 0; i--){
            if(a[i] > b[i])
                return 1;
            else if(a[i] < b[i])
                return -1;
        }
        return 0;
    }
    /*i.e: "54000" to "54"*/
    private char[] removePrefixZero(char[] charArray) {
        if(charArray == null || charArray.length == 0){
            char[] ret = new char[1];
            ret[0] = '0';
            return ret;
        }

        /*判断是否已经不需要处理，加速计算*/
        if(charArray[charArray.length - 1] != '0')
            return charArray;

        /*需要处理前面的0*/
        int length = charArray.length;
        while(length > 0 && charArray[length - 1] == '0')
            length--;

        /*长度为0返回0*/
        if(length == 0){
            char[] ret = new char[1];
            ret[0] = '0';
            return ret;
        }
        char[] ret = new char[length];
        for(int i = 0; i < length; i++)
            ret[i] = charArray[i];
        return ret;
    }
    private char[] charArrayReverse(char[] charArray) {
        if(charArray == null)
            return null;
        char[] reverseCharArray = new char[charArray.length];
        for(int i = 0; i < reverseCharArray.length; i++)
            reverseCharArray[i] = charArray[charArray.length - 1 - i];
        return reverseCharArray;
    }
    private char[] multiply(BigNum multiplicand) throws Exception{
        char[] bnb = multiplicand.getCharArrayFromBn();

        // 两数乘积位数不会超过乘数位数和+ 3位
        int csize = bn.length + bnb.length + 3;
        // 开辟乘积数组
        int[] c = new int[csize];
        // 乘积数组填充0
        for (int i = 0; i < csize; i++)
            c[i] = 0;

        // 对齐逐位相乘
        for (int j = 0; j < bnb.length; j++)
            for (int i = 0; i < bn.length; i++)
                c[i + j] +=  Integer.parseInt(String.valueOf(bn[i]))* Integer.parseInt(String.valueOf(bnb[j]));

        for (int i = 0; i < csize-1; i++) {
            int carry = c[i] / 10;
            c[i] = c[i] % 10;
            c[i + 1] += carry;
        }

        char[] ret = new char[c.length];
        for(int i = 0; i < ret.length; i++)
            ret[i] = (char)(c[i] + '0');

//        for(int i = 0; i < ret.length; i++)
//            System.out.println("ret[i] = " + ret[i]);
        ret = removePrefixZero(ret);
        return ret;
    }
    /*取前n位，比如n=3,12345表示为'54321',取出的结果为"321"表示数字123*/
    private char[] getFirstChar(char[] charArray, int n){
        //if (charArray.length < n)
        //    throw new Exception("取的数过大，无法完成");

        char[] retCharArray = new char[n];
        for(int i = 0; i < n; i++)
            retCharArray[i] = charArray[i + charArray.length - n];
        return retCharArray;
    }
    public void testPrint(String str, char[] charList) {
        System.out.print(str + " : ");
        for(int i = 0; i < charList.length; i++)
            System.out.printf("%c", charList[i]);
        System.out.println();
    }

    /*乘以10 ： 543 -> 00543*/
    private char[] charArrayPowersTen(char[] charArray, int times) throws  Exception{
        if(charArray == null || times < 0)
            throw new Exception("Err in func: charArrayPowersTen");
        if(times == 0)
            return charArrayCopy(charArray);

        char[] ret = new char[charArray.length + times];
        for(int i = 0; i < times; i++)
            ret[i] = '0';
        for(int i = 0; i < charArray.length; i++)
            ret[i+times] = charArray[i];
        return ret;
    }
    public char[] charArrayCopy(char[] arr) throws Exception {
        if (arr == null || arr.length <= 0) {
            System.err.println("ERR IN charArrayCopy : 复制字符串出错");
            throw new Exception("ERR IN charArrayCopy : 复制字符串出错");
        }
        char[] newArray = new char[arr.length];
        for(int i = 0; i < arr.length; i++)
            newArray[i] = arr[i];
        return newArray;
    }


    /**
     * Generate a new BigNum
     * i.e. if the input length is 5, might return bignum(32452) which stores as "25423" and means 32452
     * @param length
     * @return
     */
    public static BigNum generateBigNumByLength(int length) {
        char[] charArray = new char[length];
        Random rd = new Random();

        charArray[length-1] = (char)(rd.nextInt(9) + 1 + '0');
        for(int i = length-1; i >= 0; i--) {
            charArray[i] = (char)(rd.nextInt(10) + '0');
        }
        return new BigNum(charArray);
    }

    /**
     * Generate a new BigNum
     * i.e. if the input length is 5, might return bignum(32452) which stores as "25423" and means 32452
     * @param length
     * @return
     */
    public static BigNum generateBigNumByLengthForPrime(int length) {
        char[] charArray = new char[length];
        char lastIndex[] = {'1','3','7','9'};
        Random rd = new Random();

        charArray[length-1] = (char)(rd.nextInt(9) + 1 + '0');
        for(int i = length-1; i > 0; i--) {
            charArray[i] = (char)(rd.nextInt(10) + '0');
        }
        int index = rd.nextInt(4);
        //System.out.println("index = " + index);
        charArray[0] = lastIndex[index];

        return new BigNum(charArray);
    }


    /**
     * generate a bignum ranges over lowerBound and upperBound
     */
    public static BigNum generateBigNumRandom(BigNum lowerBound, BigNum upperBound) throws Exception{
        BigNum round = upperBound.bigNumSub(lowerBound);
        int length = round.length();

        /*并非完全概率均匀，不过位数足够大，其实没有区别*/
        BigNum random = generateBigNumByLength(length);
        BigNum ret = random.bigNumMod(round).bigNumAdd(lowerBound);
        //System.out.println("Generate BigNum: " + ret);
        return ret;


    }

    /**
     * MyRSA if the BigNum is prime or not
     * @param : security param, means the round to MyRSA
     */
    public boolean isPrime(int t) throws Exception{

//        long time1 = System.currentTimeMillis();

        char[] charArray = this.getCharArrayFromBn();

        /*quick MyRSA for 2 and 5*/
        if(charArray[0] == '0' || charArray[0] == '2' || charArray[0] == '4'||
                charArray[0] == '5' || charArray[0] == '6'|| charArray[0] == '8')
            return false;

//        long time2 = System.currentTimeMillis();
//        System.out.println("time2 - time1 = " + (time2-time1));


        /*bignum - 1 = 2^s * r*/
        BigNum n = this;
        BigNum r = this.bigNumSub(new BigNum(1));
        BigNum s = new BigNum(0);
        int s0 = 0;
        BigNum bigZero = new BigNum(0);
        BigNum bigOne = new BigNum(1);
        BigNum bigTwo = new BigNum(2);

//        long time3 = System.currentTimeMillis();
//        System.out.println("time3 - time2 = " + (time3-time2));


        /*s % 2 == 0 && s != 0*/
        while((int) (r.getCharArrayFromBn()[0] - '0') % 2 == 0 && !r.isSame(bigZero)) {
            s = s.bigNumAdd(bigOne);//s++;
            s0++;
            r = r.bigNumDiv(bigTwo);
        }
//        long time4 = System.currentTimeMillis();
//        System.out.println("time4 - time3 = " + (time4-time3));

        //System.out.println(this + " - 1" + " = 2 ^ " + s + " * " + r);

        //we have : n - 1 = 2^s * r
        for(int i = 0; i < t; i++) {
//            long time5 = System.currentTimeMillis();
//            time4 = time5;
//            System.out.println("time5 - time4 = " + (time5-time4));


            boolean flag = false;
            /*generate a: 2 <= a <= n-2*/
            BigNum a = generateBigNumRandom(bigTwo, this.bigNumSub(bigOne));
//            long time6 = System.currentTimeMillis();
//            System.out.println("time6 - time5 = " + (time6-time5));

            /*compute: y = a^r % n*/
            BigNum y = a.bigNumPowerModFast(r, n);
            //System.out.println("y = " + y);
//            long time7 = System.currentTimeMillis();
//            System.out.println("time7 - time6 = " + (time7-time6));




            if(y.isSame(bigOne) || y.isSame(n.bigNumSub(bigOne)))
                continue;

            for(int j = 1; j <= s0 - 1; j++) {
                y = y.bigNumPowerModFast(bigTwo, n);
                if(y.isSame(bigOne))
                    return false;
                if(y.isSame(n.bigNumSub(bigOne))) {
                    flag = true;
                    break;
                }

            }
            if(!flag)
                return false;
        }
        return true;
    }

    /**
     * BigNum - 1 = 2^s * d
     * return s
     */
    public BigNum divideTimesByTwo() throws Exception{
        BigNum ret = new BigNum(0);//int ret = 0;
        BigNum temp = this.bigNumSub(new BigNum(1)); //s = s - 1

        BigNum bigZero = new BigNum(0);
        BigNum bigOne = new BigNum(1);
        BigNum bigTwo = new BigNum(2);

        //while(s % 2 == 0)
        while(temp.bigNumMod(bigTwo).isSame(bigZero)){
            temp = temp.bigNumDiv(bigTwo); // s = s / 2;
            ret = ret.bigNumAdd(bigOne);// ret++;
        }
        System.out.println("ret = " + ret);
        return ret;
    }


    public static BigNum generatePrime(int length, int accuracy) throws Exception{
        BigNum prime;

        int round = 0;
        while(true){
            round++;
            long time1 = System.currentTimeMillis();
            prime = BigNum.generateBigNumByLengthForPrime(length);
            long time2 = System.currentTimeMillis();
            if(prime.isPrime(accuracy)){
                long time3 = System.currentTimeMillis();
                break;
            }
            long time3 = System.currentTimeMillis();
            System.out.println("Round " + round +", Generating Time"+(time2-time1) +", TestingTime: " + (time3-time2));
        }
        return prime;
    }

    /**
     * Generate number a that meets (a, n) = 1;
     * @return a
     */
    public static BigNum generateNumberPrimeTo(BigNum n) throws Exception{
        int length = n.length() / 2;
        while (true) {
            BigNum a = BigNum.generateBigNumByLength(length);
            if(BigNumGCD.gcd_b(a, n).isSame(new BigNum(1))){
                System.out.println("(a, n) = 1, a = " + a);
                return a;
            }
        }
    }
    
    public static BigNum genPrime(int length, int flag) throws Exception{
        Random rd = new Random();
        //System.out.println("gen" + rd.toString());
        BigInteger bn = BigInteger.probablePrime(length, rd);
        System.out.println("gen" + bn);
        return new BigNum(bn.toString());
    }

}
