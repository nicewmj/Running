package com.example.proxy代理;

import java.lang.reflect.*;

public class ProxyTest {
    public static void main(String[] args) throws Exception {
        /* *//**
         * 第一种 验证 接口的信息 没有构造器，只能获取方法，无法new 对象
         * Calculator接口的Class对象
         * 得到Class对象的三种方式：
         * 1.Class.forName(xxx)
         * 2.xxx.class
         * 3.xxx.getClass()
         * 注意，这并不是我们new了一个Class对象，而是让虚拟机加载并创建Class对象
         *//*
        Class<Calculator> calculatorClazz = Calculator.class;
        //Calculator接口的构造器信息
        Constructor<?>[] calculatorClazzConstructors = calculatorClazz.getConstructors();
        //Calculator接口的方法信息
        Method[] calculatorClazzMethods = calculatorClazz.getMethods();
        //打印
        System.out.println("------接口Class的构造器信息------");
        printClassInfo(calculatorClazzConstructors);
        System.out.println("\n");
        System.out.println("------接口Class的方法信息------");
        printClassInfo(calculatorClazzMethods);
        System.out.println("\n");

		*//**
         * Calculator实现类的Class对象
         *//*
		Class<CalculatorImpl> calculatorImplClazz = CalculatorImpl.class;
        //Calculator实现类的构造器信息
        Constructor<?>[] calculatorImplClazzConstructors = calculatorImplClazz.getConstructors();
        //Calculator实现类的方法信息
        Method[] calculatorImplClazzMethods = calculatorImplClazz.getMethods();
        //打印
        System.out.println("------实现类Class的构造器信息------");
        printClassInfo(calculatorImplClazzConstructors);
        System.out.println("\n");
        System.out.println("------实现类Class的方法信息------");
        printClassInfo(calculatorImplClazzMethods);*/




        /*   //第二种 通过jdk代理 实现接口 copy到一个类 并且把类的实例返回给我 实现动态的代理类实现
         *//*
         * 参数1：Calculator的类加载器（当初把Calculator加载进内存的类加载器）
         * 参数2：代理对象需要和目标对象实现相同接口Calculator
         * *//*

        Class<?> calculatorProxyClazz = Proxy.getProxyClass(Calculator.class.getClassLoader(), Calculator.class);

        //以Calculator实现类的Class对象作对比，看看代理Class是什么类型

        System.out.println(CalculatorImpl.class.getName());

        System.out.println(calculatorProxyClazz.getName());

        //打印代理Class对象的构造器

        Constructor<?>[] constructors = calculatorProxyClazz.getConstructors();

        System.out.println("----构造器----");

        printClassInfo(constructors);

        System.out.println("\n");

        //打印代理Class对象的方法

        Method[] methods = calculatorProxyClazz.getMethods();

        System.out.println("----方法----");

        printClassInfo(methods);

        System.out.println("\n");*/


        //第三种 我们利用生产的代理类获取代理对象是或否成功
        /*
         * 参数1：类加载器，随便给一个
         * 参数2：需要生成代理Class的接口，比如Calculator
         * */
        Class<?> calculatorProxyClazz = Proxy.getProxyClass(Calculator.class.getClassLoader(), Calculator.class);

        // 得到唯一的有参构造 $Proxy(InvocationHandler h)，和反射的Method有点像，可以理解为得到对应的构造器执行器
        Constructor<?> constructor = calculatorProxyClazz.getConstructor(InvocationHandler.class);

        // 用构造器执行器执行构造方法，得到代理对象。构造器需要InvocationHandler入参
        //        Calculator calculatorProxyImpl = (Calculator) constructor.newInstance((InvocationHandler) (proxy, method, args1) -> 10086);
      /*  Calculator calculatorProxyImpl = (Calculator) constructor.newInstance(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //太low了 在这里new 抽取出去
                CalculatorImpl calculator = new CalculatorImpl();
                System.out.println(method.getName()+" 方法开始执行");
                Object invoke = method.invoke(calculator, args);
                System.out.println(invoke);
                System.out.println(method.getName()+" 方法开始执行结束");
                return invoke;
            }
        });

        // 看，有同名方法！
        System.out.println(calculatorProxyImpl.add(1,2));
*/


        //  抽取方法，简化操作  第四种
/*
        CalculatorImpl target = new CalculatorImpl();
        // 传入目标对象
        Calculator calculatorProxy = (Calculator) getProxy(target);
        calculatorProxy.add(1, 2);
*/


        //第五种
        // 1.得到目标对象
  /*      CalculatorImpl target = new CalculatorImpl();
        // 2.传入目标对象，得到增强对象 就是日志增强的打印记录 （如果需要对目标对象进行别的增强，可以另外编写getXxInvocationHandler）
        InvocationHandler logInvocationHandler = getLogInvocationHandler(target);
        // 3.传入目标对象+增强代码，得到代理对象
        Calculator calculatorProxy = (Calculator) getProxy(target, logInvocationHandler);
        calculatorProxy.add(1, 2);
*/

        /**
         更好用的API：Proxy.newProxyInstance()
         目前为止，我们学习都是Proxy.getProxyClass()：
         • 先获得proxyClazz
         • 再根据proxyClazz.getConstructor()获取构造器
         • 最后constructor.newInstance()生成代理对象

         JDK已经提供了一步到位的方法    Proxy.newProxyInstance()
         */

        // 1.得到目标对象

        CalculatorImpl target = new CalculatorImpl();
        // 2.传入目标对象，得到增强对象（如果需要对目标对象进行别的增强，可以另外编写getXxInvocationHandler）
        InvocationHandler logInvocationHandler = getLogInvocationHandler(target);
        // 3.传入目标对象+增强代码，得到代理对象（直接用JDK的方法！！！）
        Calculator calculatorProxy = (Calculator) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),   // 随便传入一个类加载器
                target.getClass().getInterfaces(),    // 需要代理的接口
                logInvocationHandler                  // 增强对象（包含 目标对象 + 增强代码）

        );
        calculatorProxy.add(1, 2);

    }

    /**
     * //第五种
     * 传入目标对象+增强代码，获取代理对象
     */

    private static Object getProxy(final Object target, final InvocationHandler handler) throws Exception {
        // 参数1：随便找个类加载器给它 参数2：需要代理的接口
        Class<?> proxyClazz = Proxy.getProxyClass(target.getClass().getClassLoader(), target.getClass().getInterfaces());
        Constructor<?> constructor = proxyClazz.getConstructor(InvocationHandler.class);
        return constructor.newInstance(handler);
    }


    /**
     * //第五种
     * 日志增强代码
     */

    private static InvocationHandler getLogInvocationHandler(final CalculatorImpl target) {
        return new InvocationHandler() {
            @Override
            public Object invoke(Object proxy1, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName() + "方法开始执行...");
                Object result = method.invoke(target, args);
                System.out.println(result);
                System.out.println(method.getName() + "方法执行结束...");
                return result;

            }

        };

    }


    /**
     * 第四种
     * 传入目标对象，获取代理对象
     * <p>
     * 上面的代码还有问题：虽然传入任意对象我们都可以返回增强后的代理对象，但增强代码是写死的。
     * 如果我需要的增强不是打印日志而是其他操作呢？难道重新写一个getProxy()方法吗？所以，我们应该抽取InvocationHander
     * 将增强代码和代理对象解耦（其实重写getProxy()和抽取InvocationHander本质相同，但后者细粒度小一些）。
     */

    private static Object getProxy(final Object target) throws Exception {
        // 参数1：随便找个类加载器给它 参数2：需要代理的接口
        Class<?> proxyClazz = Proxy.getProxyClass(target.getClass().getClassLoader(), target.getClass().getInterfaces());
        Constructor<?> constructor = proxyClazz.getConstructor(InvocationHandler.class);
        return constructor.newInstance(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy1, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName() + "方法开始执行...");
                Object result = method.invoke(target, args);
                System.out.println(result);
                System.out.println(method.getName() + "方法执行结束...");
                return result;
            }
        });
    }


    public static void printClassInfo(Executable[] targets) {
        for (Executable target : targets) {
            // 构造器/方法名称
            String name = target.getName();
            StringBuilder sBuilder = new StringBuilder(name);
            // 拼接左括号
            sBuilder.append('(');
            Class<?>[] clazzParams = target.getParameterTypes();
            // 拼接参数
            for (Class<?> clazzParam : clazzParams) {
                sBuilder.append(clazzParam.getName()).append(',');
            }
            //删除最后一个参数的逗号
            if (clazzParams.length != 0) {
                sBuilder.deleteCharAt(sBuilder.length() - 1);
            }
            //拼接右括号
            sBuilder.append(')');
            //打印 构造器/方法
            System.out.println(sBuilder.toString());
        }
    }
}