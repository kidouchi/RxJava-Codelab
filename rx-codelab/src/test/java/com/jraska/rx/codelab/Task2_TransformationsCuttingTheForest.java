package com.jraska.rx.codelab;

import com.jraska.rx.codelab.forest.BasketOfWood;
import com.jraska.rx.codelab.forest.Fireplace;
import com.jraska.rx.codelab.forest.Firewood;
import com.jraska.rx.codelab.forest.Forest;
import com.jraska.rx.codelab.forest.Log;
import com.jraska.rx.codelab.forest.Lumberjack;
import com.jraska.rx.codelab.forest.Tools;
import com.jraska.rx.codelab.forest.Tree;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import org.junit.Before;
import org.junit.Test;

import io.reactivex.Observable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Task2_TransformationsCuttingTheForest {
  Fireplace fireplace;

  @Before
  public void before() {
    fireplace = new Fireplace();
  }

  @Test
  public void map_fromOnePieceExactlyToOnePieceOfOtherStuff() {
    Observable<Tree> treeObservable = Lumberjack.cut(Forest.AMAZON);

    // TODO: Transform Observable of Trees to Observable of Firewood. Tools like handSaw and chopping can be useful
    Observable<Log> logObservable = treeObservable.map(tree -> Tools.handSaw(tree));

    Observable<Firewood> woodObservable = logObservable.map(log -> Tools.chop(log));

    fireplace.subscribeForBurn(woodObservable);
  }

  @Test
  public void flatMap_chainSawProducesMoreLogs() {
    // TODO:  Cutting wood by handSaw is not effective, lets use chainSaw now
    Observable<Firewood> woodObservable = Lumberjack.cut(Forest.AMAZON)
      .flatMap((Function<Tree, ObservableSource<Log>>) tree -> Tools.chainSaw(tree))
      .map(log -> Tools.chop(log));

    fireplace.subscribeForBurn(woodObservable);
  }

  @Test
  public void buffer_createBasketsOfWood() {
    // TODO: We want some bigger fire, lets put firewood into baskets. Buffering int BasketOfWood might be useful

    Observable<BasketOfWood> basketsObservable = Lumberjack.cut(Forest.AMAZON)
      .flatMap((Function<Tree, ObservableSource<Log>>) tree -> Tools.chainSaw(tree))
      .map(log -> Tools.chop(log))
      .buffer(BasketOfWood.BASKET_SIZE)
      .map(firewoods -> new BasketOfWood(firewoods));

    fireplace.subscribeForBasketBurn(basketsObservable);
  }

  @Test
  public void flatMap_buffer_createBasketsOfWoodWithCuttingMachine() {
    // TODO: Lets be even more effective and use machineChop tool to get

    Observable<BasketOfWood> basketsObservable = Lumberjack.cut(Forest.AMAZON)
      .flatMap((Function<Tree, ObservableSource<Log>>) tree -> Tools.chainSaw(tree))
      .flatMap((Function<Log, ObservableSource<Firewood>>) log -> Tools.machineChop(log))
      .buffer(BasketOfWood.BASKET_SIZE)
      .map(firewoods -> new BasketOfWood(firewoods));
    fireplace.subscribeForBasketBurn(basketsObservable);
  }

  @Test
  public void debounce_filterTooMuchWoodAtOnce() {
    Observable<Log> logsObservable = Lumberjack.cut(Forest.AMAZON)
      .flatMap((tree) -> {
        Thread.sleep(tree.treeOrdinal() * 100);
        return Tools.chainSaw(tree);
      });

    // TODO: Fireplace is able to handle only one firewood per 250 milliseconds, use debounce for inputs to filter too much firewood out
    Observable<Firewood> woodObservable =
      logsObservable
        .debounce(250, TimeUnit.MILLISECONDS)
        .map(Tools::chop);


    fireplace.subscribeForBurn(woodObservable);
  }
}
