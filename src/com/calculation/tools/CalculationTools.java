package com.calculation.tools;

import java.util.concurrent.ThreadLocalRandom;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * 用来辅助游戏计算的类.
 *
 * @author 留恋千年
 * @version 1.1.0
 * @since 2021-7-28
 */
public class CalculationTools
{
    private CalculationTools()
    {
        throw new AssertionError();
    }

    /**
     * 用来辅助游戏数值计算的类.
     *
     * @author 留恋千年
     * @version 1.0.0
     * @since 2021-7-29
     */
    public static class Value
    {
        private Value()
        {
            throw new AssertionError();
        }

        /**
         * 根据给定的数值计算攻击者的攻击命中的几率.
         *
         * @param attackerHit 攻击者的命中
         * @param victimEvade 被攻击者的闪避
         * @return 攻击者的攻击命中的几率
         * @since 2021-5-15
         */
        public static double attackHitRate(final int attackerHit, final int victimEvade)
        {
            if (attackerHit <= 0)
            {
                return 1.0;
            }
            if (victimEvade <= 0)
            {
                return 0.0;
            }
            return (double) attackerHit / (attackerHit + victimEvade);
        }

        /**
         * 计算攻击者的攻击暴击的概率.
         *
         * @param attackerCrit     攻击者的暴击
         * @param victimResistance 被攻击者的暴击抗性
         * @return 攻击者的攻击暴击的概率
         * @since 2021-5-16
         */
        public static double attackerCritChance(final int attackerCrit, final int victimResistance)
        {
            if (attackerCrit <= 0)
            {
                return 0.0;
            }
            if (victimResistance <= 0)
            {
                return 1.0;
            }
            return (double) attackerCrit / (attackerCrit + victimResistance);
        }

        /**
         * 用给定的数值计算攻击者能对被攻击者打出的伤害.
         *
         * @param attackerPhysicalAttack 攻击者的物理攻击
         * @param victimArmor            被攻击者的护甲值
         * @return 攻击者的伤害
         */
        public static double attackerPhysicalDamage(double attackerPhysicalAttack, double victimArmor)
        {
            var attack = attackerPhysicalAttack;
            var armor = victimArmor;

            //为了防止出现NaN错误
            if (attackerPhysicalAttack + victimArmor == 0)
            {
                if (victimArmor <= 0)
                {
                    attack = ++attackerPhysicalAttack;
                }
                if (attackerPhysicalAttack <= 0)
                {
                    armor = ++victimArmor;
                }
            }
            return attack * attack / (attack + armor);
        }

        /**
         * 计算被攻击者有效HP.
         *
         * @param victimHp        被攻击者的HP
         * @param damageReduction 伤害减免率
         * @param evadeChance     闪避概率
         * @return 返回被攻击者有效HP
         */
        public static double victimEffectiveHp(final int victimHp, final double damageReduction, final double evadeChance)
        {
            if (evadeChance >= 1.0 || damageReduction >= 1.0)
            {
                return Integer.MAX_VALUE;
            }
            return victimHp / (1.0 - damageReduction) / (1.0 - evadeChance);
        }

        /**
         * 计算攻击者对被攻击者的暴击伤害.
         *
         * @param hurt        攻击者对被攻击者可以造成的的伤害
         * @param critsEffect 攻击者的暴击效果
         * @return 攻击者对被攻击者的暴击伤害
         */
        public static int criticalDamage(final double hurt, final double critsEffect)
        {
            return Math.round((float) (hurt * critsEffect));
        }
    }

    /**
     * 用来辅助计算的类.
     *
     * @author 留恋千年
     * @version 1.1.0
     * @since 2021-7-28
     */
    public static class Tools
    {
        private Tools()
        {
            throw new ArithmeticException();
        }
        public enum SpecifiedDirection
        {
            /**仅增加*/
            ONLY_INCREASE,
            /**仅减少*/
            ONLY_REDUCED
        }

        /**
         * 根据给定的概率返回{@code true}.
         *
         * @param trueProbability 返回{@code true}的概率
         * @return 根据给定概率返回 {@code true}
         */
        public static boolean randomBooleanValue(double trueProbability)
        {
            if (trueProbability >= 1.0)
            {
                return true;
            }
            if (trueProbability <= 0.0)
            {
                return false;
            }
            return ThreadLocalRandom.current().nextDouble() < trueProbability;
        }

        /**
         * 随机对数字加或减一个范围内的数.
         *
         * @param number 要进行加工的整数
         * @param floatingIntRange 浮动的整数范围(非负数)
         * @return {@code number}加或减 0(包含) ~ {@code floatingIntRange}(包含)的一个数
         * @throws IllegalArgumentException 如果{@code floatingIntRange}小于0
         */
        public static int floatingNumber(final int number, final int floatingIntRange)//按整数浮动
        {
            if (floatingIntRange < 0)
            {
                throw new IllegalArgumentException("错误范围:" + floatingIntRange);
            }

            var randomNumber = current().nextInt(floatingIntRange + 1);
            return current().nextBoolean() ? number + randomNumber : number - randomNumber;
        }

        /**
         * 随机对数字加或减一个范围内的数.
         *
         * @param number 要进行加工的整数
         * @param floatingIntRange 浮动的整数范围(非负数)
         * @param sign 手动指定的浮动方向, 只支持(+, -)
         * @return {@code number}加或减(根据{@code sign}的值来决定) 0(包含) ~ {@code floatingIntRange}(包含)的一个数
         * @throws IllegalArgumentException 如果{@code floatingIntRange}小于0或sign的值是非法的
         * @throws NullPointerException 如果{@code sign}为null
         */
        public static int floatingNumber(final int number, final int floatingIntRange,
                                         final SpecifiedDirection sign)//按整数浮动
        {
            if (floatingIntRange < 0)
            {
                throw new IllegalArgumentException("错误范围:" + floatingIntRange);
            }
            requireNonNull(sign);

            var randomNumber = current().nextInt(floatingIntRange + 1);
            return switch (sign) {
                case ONLY_INCREASE -> number + randomNumber;
                case ONLY_REDUCED -> number - randomNumber;
            };
        }

        /**
         * 随机对数字加或减一个范围内的数.
         *
         * @param number 要进行加工的整数
         * @param floatingPercentage 浮动的百分比范围
         * @return {@code number}加或减 0(包含) ~ {@code number * floatingPercentage}(包含)的一个数
         * @throws IllegalArgumentException 如果{@code floatingPercentage}小于0.0
         */
        public static int floatingNumber(final int number, final double floatingPercentage)
        {
            if (floatingPercentage < 0.0)
            {
                throw new IllegalArgumentException("错误范围:" + floatingPercentage);
            }

            var randomNumber = current().nextInt((int) floatingPercentage * number + 1);
            return current().nextBoolean() ? number + randomNumber : number - randomNumber;
        }

        /**
         * 随机对数字加或减一个范围内的数.
         *
         * @param number 要进行加工的整数
         * @param floatingPercentage 浮动的百分比范围
         * @return {@code number}加或减(根据{@code sign}的值来决定) 0(包含) ~ {@code number * floatingPercentage}(包含)的一个数
         * @param sign 手动指定的浮动方向, 只支持(+, -)
         * @throws IllegalArgumentException 如果{@code floatingPercentage}小于0.0或sign的值是非法的
         * @throws NullPointerException 如果{@code sign}为null
         */
        public static int floatingNumber(final int number, final double floatingPercentage, final SpecifiedDirection sign)
        {
            if (floatingPercentage < 0.0)
            {
                throw new IllegalArgumentException("错误范围:" + floatingPercentage);
            }
            requireNonNull(sign);

            var randomNumber = current().nextInt((int) floatingPercentage * number + 1);
            return switch (sign) {
                case ONLY_INCREASE -> number + randomNumber;
                case ONLY_REDUCED -> number - randomNumber;
            };
        }

        public static int floatingNumber(final double number, final double floatingPercentage)
        {
            if (floatingPercentage < 0.0)
            {
                throw new IllegalArgumentException("错误范围:" + floatingPercentage);
            }

            final int max = Math.round((float) (floatingPercentage * number)) + 1;
            var randomNumber = current().nextInt(max);
            return (int) (current().nextBoolean() ? number + randomNumber : number - randomNumber);
        }
    }
}
