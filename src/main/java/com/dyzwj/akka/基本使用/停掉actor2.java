package com.dyzwj.akka.基本使用;

import akka.actor.*;

/**
 * 停止 Actor 大致有三种方式，它们分别是：
 * ● 调用 ActorSystem 或者 getContext() 的 stop 方法；
 * ● 给 Actor 发送一个 PoisonPill （毒丸）消息；
 * ● 给 Actor 发送一个 Kill 的消息，此时会抛出ActorKilledException 异常，并上报到父级 supervisor 处理。
 */
public class 停掉actor2 {

    static class SimpleActor extends AbstractActor{

        public static Props props(){
            return Props.create(SimpleActor.class, SimpleActor::new);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny((msg) -> {
                System.out.println(msg);
            }).build();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create("sys");
        ActorRef actorRef = actorSystem.actorOf(SimpleActor.props(), "simpleActor");
        actorRef.tell("1",ActorRef.noSender());
        actorRef.tell("2",ActorRef.noSender());

        //对比actorSystem.stop() 为啥不需要sleep()也能正确消费 1,2,3
        //原理执行此行代码之前  邮箱元素["1","2"]
        actorRef.tell(PoisonPill.getInstance(),ActorRef.noSender());
        //执行此行代码后 邮箱元素["1","2","PoisonPill"]

        actorRef.tell(Kill.getInstance(),ActorRef.noSender());
        //执行此行代码后 邮箱元素["1","2","Kill"]
        //在PoisonPill或Kill消息之前的消息，都能消费成功消费  优雅停止
        actorRef.tell("3",ActorRef.noSender());

    }
}
