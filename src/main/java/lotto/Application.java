package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {
    public static void main(String[] args) {

        System.out.println("구입금액을 입력해 주세요.");
        int money = Integer.parseInt(Console.readLine());
        int count = money / 1000;
        System.out.println(count + "개를 구매했습니다.");

        Map<Integer, List<Integer>> reception = new HashMap<>();
        for (int i = 0; i < count; i++) {
            List<Integer> numbers = Randoms.pickUniqueNumbersInRange(1, 45, 6);
            List<Integer> sortedNumbers = numbers.stream().sorted().toList();
            System.out.println(sortedNumbers);
            reception.put(i, sortedNumbers);
        }

        System.out.println();

        System.out.println("당첨 번호를 입력해 주세요.");
        String[] answerInput = Console.readLine().split(",");
        List<Integer> answers = Arrays.stream(answerInput).mapToInt(Integer::parseInt).boxed()
                .collect(Collectors.toList());
        //        answers = Collections.singletonList(Integer.parseInt(Arrays.toString(answerInput)));

        System.out.println("보너스 번호를 입력해 주세요.\n");
        int bonus = Integer.parseInt(Console.readLine());
        Map<Integer, Long> gainStatics = new HashMap<>();
        gainStatics.put(3, 0L);
        gainStatics.put(4, 0L);
        gainStatics.put(5, 0L);
        gainStatics.put(6, 0L);
        gainStatics.put(7, 0L);
        System.out.println("당첨 통계");
        System.out.println("---");
        for (int i = 0; i < count; i++) {
            long matchCount = 0;
            boolean isBonus = false;

            if (reception.get(i) != null) {
                matchCount = reception.get(i).stream().filter(answers::contains).count();
            } else if (reception.get(i) == null) {

            }
            isBonus = reception.get(i).contains(bonus);
            if (matchCount == 3) {
                gainStatics.put(3, gainStatics.get(3) + 1);
            } else if (matchCount == 4) {
                gainStatics.put(4, gainStatics.get(4) + 1);
            } else if (matchCount == 5 && isBonus) {
                gainStatics.put(5, gainStatics.get(6) + 1);
            } else if (matchCount == 5) {
                gainStatics.put(6, gainStatics.get(5) + 1);
            } else if (matchCount == 6) {
                gainStatics.put(7, gainStatics.get(7) + 1);
            }
        }
        Map<Integer, String> price = new HashMap<>();
        price.put(3, "5,000");
        price.put(4, "50,000");
        price.put(5, "1,500,000");
        price.put(6, "30,000,000");
        price.put(7, "2,000,000,000");
        long profit = 0L;
        for (int i = 3; i < 8; i++) {
            int amount = i;
            if (amount < 6) {
                System.out.println(message(amount, price.get(amount), gainStatics.get(i)));
                profit += calculateProfit(gainStatics.get(amount), price.get(amount));
            }
            if (amount == 5) {
                System.out.println(meesage(amount, price.get(amount + 1), gainStatics.get(i), true));
                profit += calculateProfit(gainStatics.get(amount), price.get(amount));
            }
            if (amount > 6) {
                System.out.println(message(amount - 1, price.get(amount), gainStatics.get(i)));
                profit += calculateProfit(gainStatics.get(amount), price.get(amount));

            }
        }

        // 숫자 리스트 정렬
        // 숫자 6자리 입력 하고, 보너스 입력 하기
        // 해당 당첨 기준에 맞게 출력 하기
        // 수익률 산출 ( 수식 )
        double profitRate = ((double) profit / money) * 100;
        BigDecimal roundedProfitRate = BigDecimal.valueOf(profitRate).setScale(2, RoundingMode.HALF_UP);
        System.out.println("총 수익률은 " + String.format("%.1f", roundedProfitRate) + "%입니다.");
    }

    private static String message(int mount, String price, Long count) {
        StringBuilder sb = new StringBuilder();
        sb.append(mount);
        sb.append("개 일치 (");
        sb.append(price);
        sb.append("원)");
        sb.append(" - ");
        sb.append(count.intValue());
        sb.append("개");
        return sb.toString();
    }

    private static String meesage(int mount, String price, Long count, boolean isBonus) {
        StringBuilder sb = new StringBuilder();
        if (isBonus) {
            sb.append(mount);
            sb.append("개 일치, 보너스 볼 일치 (");
            sb.append(price);
            sb.append("원)");
            sb.append(" - ");
            sb.append(count.intValue());
            sb.append("개");
        }
        return sb.toString();
    }

    private static long calculateProfit(long multi, String priceString) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            Number number = numberFormat.parse(priceString);
            return multi * number.longValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
