package thu.infosecurity.simulate.util.NumberTheory;

public class CRT {
    public static void main(String[] args){
        System.out.println("Helloworld");
        System.out.println(new CRT().getReverseByEcliuid(343, 65467));
    }
    /**
     * @Func: get the reverse of a
     * @Ret : b, b = 1 (mod a)
     */
    public int getReverseWayOne(int a){
        return 0;
    }
    /**
     * @Func: get the reverse of a
     * @Ret : b, b*a = 1 (mod mod)
     * @Way : Ecliuid
     */
    public int getReverseByEcliuid(int a, int mod){
        int ret = (new GCD()).getuv(a, mod).u;
        while(ret < 0)
            ret += mod;
        return ret;
    }

    public int getPhi(int n){
        if(isPrime(n))
            return n - 1;
        System.err.println(":::TO BE FINISHED");
        //TODO
        return 0;
    }


    public boolean isPrime(int n){
        for(int i = 2; i < Math.sqrt(n); i++)
            if(n % i == 0)
                return false;
        return true;
    }

}

