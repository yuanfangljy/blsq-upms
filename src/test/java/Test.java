
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
 
 
public class Test {

    public static void main(String[] args) {
        List<String> list1 = new ArrayList<String>();
        list1.add("0ccfb8bd75661be58be3f9594979cc34");
        list1.add("11111111111111111111111111111111");
        list1.add("3333333333333333333333333333333");

        List<String> list2 = new ArrayList<String>();
        list2.add("0ccfb8bd75661be58be3f9594979cc34");
        list2.add("8b4a0aff-332c-11e9-aaaf-0242ac110004");
        list2.add("222222222222222222222222222222222");

        // 交集
        List<String> intersection = list1.stream().filter(item -> list2.contains(item)).collect(toList());
        System.out.println("---交集 intersection---");
        intersection.parallelStream().forEach(System.out::println);

        // 差集 (list1 - list2)
        List<String> reduce1 = list1.stream().filter(item -> !list2.contains(item)).collect(toList());
        System.out.println("---差集 reduce1 (list1 - list2)---");
        reduce1.parallelStream().forEach(System.out::println);

        System.out.println("============================================================");
        List<String> ir=new ArrayList<>();
        if(list1.size()==list2.size()){
            List<String> reduce12 = list1.stream().filter(item -> !list2.contains(item)).collect(toList());
            ir.addAll(reduce12);
            List<String> reduce2 = list2.stream().filter(item -> !list1.contains(item)).collect(toList());
            ir.addAll(reduce2);
        }
        System.out.println("---差集 reduce1 (list1 == list2)---");
        ir.parallelStream().forEach(System.out::println);
        System.out.println("============================================================");



        // 差集 (list2 - list1)
        List<String> reduce2 = list2.stream().filter(item -> !list1.contains(item)).collect(toList());
        System.out.println("---差集 reduce2 (list2 - list1)---");
        reduce2.parallelStream().forEach(System.out::println);

        // 并集
        List<String> listAll = list1.parallelStream().collect(toList());
        List<String> listAll2 = list2.parallelStream().collect(toList());
        listAll.addAll(listAll2);
        System.out.println("---并集 listAll---");
        listAll.parallelStream().forEachOrdered(System.out::println);

        // 去重并集
        List<String> listAllDistinct = listAll.stream().distinct().collect(toList());
        System.out.println("---得到去重并集 listAllDistinct---");
        listAllDistinct.parallelStream().forEachOrdered(System.out::println);

        System.out.println("---原来的List1---");
        list1.parallelStream().forEachOrdered(System.out::println);
        System.out.println("---原来的List2---");
        list2.parallelStream().forEachOrdered(System.out::println);

    }
}
