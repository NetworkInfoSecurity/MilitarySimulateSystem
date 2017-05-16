package thu.infosecurity.simulate.util.NumberTheory;
import thu.infosecurity.simulate.util.BaseElement.uvCuple;

/**
 * @author ForestNeo
 *
 */


public class GCD {

    public static void main(String[] args){
        GCD testGcd = new GCD();

//        int n = testGcd.mod_times(456, 23462, 66473);
//        System.out.println(n);
//
//		System.out.println("Testing gcd()");
//		System.out.println(testGcd.gcd(343254356,23456));
//		System.out.println("Testing B_gcd()");
//		System.out.println(testGcd.B_gcd(343254356,23456));

//        System.out.println("Testing getuv");

        int a = 17, b = 23;

        uvCuple test1 = GCD.getuv(a, b);
        System.out.println(testGcd.gcd(a,b) + " = " + test1.u + " * " + a + " + " + test1.v + " * " + b);
        uvCuple test2 = GCD.getuvtest1(a, b);
        System.out.println(testGcd.gcd(a,b) + " = " + test2.u + " * " + a + " + " + test2.v + " * " + b);

    }


    public int gcd(int a, int b) {
        if(a % b == 0)
            return b;
        return gcd(b, a % b);
    }

    /**
     * the balanced Euclidean algorithm
     */
    public int B_gcd(int a, int b){
        if(a % b == 0)
            return b;
        return  a % b > b / 2 ? B_gcd(b, b - a % b) : B_gcd(b, a % b);
    }

    /**
     * get u and v that satifies u*a + v*b = gcd(a,b)
     */
    public static uvCuple getuv(int a, int b){
        if(b % (a % b) == 0){
            uvCuple ret = new uvCuple(1, -(a/b));
            return ret;
        }
        uvCuple newpair = getuv(b, a % b);
		/*a = a/b * b + a % b;*/
        int x = newpair.v;
        int y = newpair.u - a / b * newpair.v;
        newpair.u = x;
        newpair.v = y;
//		System.out.println("(a,b) = (" + newpair.u +"," + newpair.v +")");
        return newpair;
    }


    /**
     * count y = base^times mod(mod)
     * @return
     */
    public int mod_times(int base, int times, int mod){
        int ret = 1;
        while(times-->0){
            ret = ret * base % mod;
        }
        return ret;
    }




    public static uvCuple getuvtest1(int a, int b){
        return getuvtest2(a, b, b);
    }

    public static uvCuple getuvtest2(int a, int b, int c){
        if(b % (a % b) == 0){
            uvCuple ret = new uvCuple(1, (-(a/b) + c)%c);
            return ret;
        }
        uvCuple newpair = getuv(b, (a % b+c)%c);
		/*a = a/b * b + a % b;*/
        int x = newpair.v;
        int y = newpair.u - a / b * newpair.v;
        newpair.u = (x+c)%c;
        newpair.v = (y+c)%c;
//		System.out.println("(a,b) = (" + newpair.u +"," + newpair.v +")");
        return newpair;
    }


}
