package shop.yesaladin.shop.order.domain.repository;

import shop.yesaladin.shop.order.domain.model.Subscribe;

public interface CommandSubscribeRepository {

    Subscribe save(Subscribe subscribe);
}
