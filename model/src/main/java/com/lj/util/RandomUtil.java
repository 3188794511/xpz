package com.lj.util;

import java.util.*;
import java.util.stream.IntStream;

public class RandomUtil {

    /**
     * 生成nums.length个在rangIds中的id
     * @param excludeId 需要排除的值
     * @param nums 生成list的长度
     * @param rangeIds 随机值的范围
     * @return
     */
    public static List<Long> generateIds(Long excludeId,Integer nums,List<Long> rangeIds){
        List<Long> res = new ArrayList<>();
        int max = rangeIds.size();
        int min = 0;
        while(res.size() < nums) {
            IntStream intStream = new Random().ints(min, max);
            int randomIndex = intStream.findFirst().getAsInt();
            Long randomVal = rangeIds.get(randomIndex);
            if(!res.contains(randomVal)){
                //未出现过该随机值
                if(Objects.nonNull(excludeId)){
                    if(!excludeId.equals(randomVal)){
                        res.add(randomVal);
                    }
                }
                else {
                    res.add(randomVal);
                }
            }
        }
        return res;
    }

    public static  List<Long> generateIds(Integer nums,List<Long> rangeIds){
        return generateIds(null,nums,rangeIds);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(generateIds(null,5, Arrays.asList(3L,2L,1L,5L,6L,7L,8L,9L)));
    }
}
