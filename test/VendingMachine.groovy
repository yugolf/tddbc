
import spock.lang.*
import sun.rmi.transport.tcp.TCPEndpoint
import sun.plugin.cache.FileVersion;
/**
 * Created with IntelliJ IDEA.
 * User: maede
 * Date: 12/06/03
 * Time: 13:38
 * To change this template use File | Settings | File Templates.
 */
class VendingMachine extends spock.lang.Specification{
     private List<Integer> inputMoney = new ArrayList<Integer>();
    private Map<Juice, Integer> juiceStock = new HashMap<Juice, Integer>();

//    public VendingMachine() {
//       this.juiceStock.put(Juice.COKE, 5)
//    }

     public enum ValidMoney {
        TEN(10),
        FIFTY(50),
        ONE_HUNDRED(100),
        FIVE_HUNDRED(500),
        ONE_THOUSAND(1000);
        private final int money;
        ValidMoney(int money) {
            this.money = money
        }
         int toInteger() {
             return this.money;
         }
     }

    enum Juice {
        COKE(1, "コーラ", 120);
        private final int id;
        private final String name;
        private final int price;
        Juice(int id, String name, int price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
        String getName() {
            return this.name;
        }
        int getPrice() {
            return this.price;
        }
    }


    @Unroll
    def "#input  をいれると #sum になる"(){
        expect:
        vending(input) == sum

        where:
        input                      |sum
        ValidMoney.TEN             | 10
        ValidMoney.FIFTY           | 50
        ValidMoney.ONE_HUNDRED     | 100
        ValidMoney.ONE_THOUSAND    | 1000

    }

    def "複数回硬貨を投入すると、総計が取得できる"() {
        expect:
        input.eachWithIndex {it, i ->
            def r = vending(it)
            if(input.size() == i+1){
              r == sum
            }
        }

        where:
        input       | sum
        [ValidMoney.TEN,ValidMoney.FIFTY]     | 60
    }

    def "払い戻し操作をすると総計を総計を出力する。"() {
        given:

        expect:
        input.eachWithIndex {it, i ->
            vending(it)
            if(input.size() == i+1){
                def r = refund()
                r == sum
            }
        }


        where:
        input       | sum
        [ValidMoney.TEN]        | 10
        [ValidMoney.TEN, ValidMoney.FIFTY]    | 60
    }

//    def "想定外のものが投入された場合は、投入金額に加算せず、そのまま釣り銭として出力する。"(){
//        expect:
//        vending(input) == ret
//        refund()  ==  sum
//
//        where:
//        input   | ret     | sum
//        1       | 1     | 0
//    }

//    def "初期状態（コーラ１２０円を５本格納）の情報を取得できる"(){
//
//        expect:
//        def jucieStock = getJucieStock()
//        def coke == juiceStock.get(Juice.COKE)
//        && cock.getName() == name
//        && jucieStock.getStock() = stock
//
//        where:
//        price | name    |  stock
//        120   | "コーラ"  |5
//
//
//    }


    private int vending(ValidMoney input){
        inputMoney.add(input);

        return sumMoney();
    }

    private int sumMoney() {
        Integer sum = 0;
        for(ValidMoney money : inputMoney){
            sum += money.toInteger();
        }
        return sum;
    }

    private int refund() {
        return sumMoney();
    }

//    private JuiceStock getJucieStock(){
//        return Collections.unmodifiableMap(this.juiceStock);
//    }
}
