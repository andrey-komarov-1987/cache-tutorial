# Назначение
Демонстрация кеширования с использованием различных реализаций и стандартов

Реализации кэширования
* [Caffeine](https://github.com/ben-manes/caffeine)
* [Redis](https://redis.io/) с использованием [Redisson](https://redisson.org/)

Стандарты
* [Spring cache](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/cache.html)
* [JSR 107](https://www.javadoc.io/doc/javax.cache/cache-api/1.0.0/javax/cache/package-summary.html)

## Мотивация использования JSR 107 в Spring
* Spring cache не предоставляет методов для атомарного доступа к элементам кэша, см. [Cache](org.springframework.cache.Cache).
Атомарный доступ необходим для кэшей, элементы которых должны быть использованы только один раз. Например, коды подтверждения, токены на скачивание.
В JSR 107 атомарный доступ реализован, см. `javax.cache.Cache.getAndRemove`
* Spring cache не регламентирует ttl кэша. В JSR 107 есть возможность единообразного указания `ttl`, см. `javax.cache.configuration.MutableConfiguration.setExpiryPolicyFactory`

## АПИ приложения
Приложение предоставляет rest АПИ. 
Коллекция запросов в `postman/cache.postman_collection.json`.
Основные элементы
* `cache/spring` - Spring реализация;
* `cache/jsr107` - JSR 107 реализация;
* `provider` - реализация кэша: `caffeine|redis`, см. `com.example.cachetutorial.service.CacheManagerResolver`;
* `cache` - наименование кэша;
* `key` - наименование ключа
