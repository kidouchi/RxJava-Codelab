package com.jraska.rx.codelab;

import io.reactivex.Observable;
import org.junit.Test;

public class Task1_Basics {
  @Test
  public void dummyObservable() {
    // TODO:  Create Observable with single String value, subscribe to it and print it to console (Observable.just)
    Observable.just("hello")
      .subscribe(s -> System.out.println(s));

  }

  @Test
  public void arrayObservable() {
    // TODO:  Create Observable with ints 1, 2, 3, 4, 5, subscribe to it and print each value to console (Observable.fromArray)
    Observable.fromArray(1,2,3,4,5)
      .subscribe(integer -> System.out.println(integer));
  }

  @Test
  public void helloOperator() {
    // TODO:  Create Observable with ints 1 .. 10 subscribe to it and print only odd values (Observable.range, observable.filter)
    Observable.range(1, 11)
      .filter(integer -> integer % 2 != 0)
      .subscribe(integer -> System.out.println(integer));
  }

  @Test
  public void receivingError() {
    // TODO:  Create Observable which emits an error and print the console (Observable.error), subscribe with onError handling
    Observable.error(new Throwable("Test error"))
      .subscribe(o -> {
        // do nothing
      }, throwable -> System.out.println(throwable.getMessage()));
  }
}
