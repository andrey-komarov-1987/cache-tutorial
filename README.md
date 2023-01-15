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

### Кэширование
* `cache/spring` - Spring реализация;
* `cache/jsr107` - JSR 107 реализация;
* `cache/redisson/r-local-cached-map` - Redisson реализация с поддержкой локального хранения ключей;
* `cache/redisson/r-map-cache` - Redisson реализация

Параметры
* `provider` - реализация кэша: `caffeine|redis`, см. `com.example.cachetutorial.service.cache.CacheManagerResolver`;
* `cache` - наименование кэша;
* `key` - наименование ключа

### Распределенная блокировка
`lock/redisson`

### Ключи 
`keys/redisson`

## Мониторинг Redis
Для [мониторинга](https://redis.io/commands/monitor/) Redis можно использовать команду
```shell
redis-cli monitor | grep -iv ping
```

# Выбор объекта Redisson
Для использования в качестве кэша были рассмотрены следующие объекты `Redisson`
* `org.redisson.api.RLocalCachedMap` - кэш хранится локально, с использованием `caffeine` или собственной реализации `Redisson`. 
  Кэш может сохраняться в Redis, см. `org.redisson.api.LocalCachedMapOptions.storeMode`, в виде ключей [хэш-таблицы](https://redis.io/docs/data-types/hashes/)
* `org.redisson.api.RMapCache` - кэш хранится в Redis в виде ключей [хэш-таблицы](https://redis.io/docs/data-types/hashes/)
* `org.redisson.api.RBucket` - кэш хранится в Redis в виде [объекта](https://redis.io/docs/data-types/strings/). 
  Несмотря на то, что тип данных называется строкой, он позволяет сохранять объекты:
> Redis strings store sequences of bytes, including text, serialized objects, and binary arrays

Redis не позволяет указывать срок хранения для отдельных ключей хэш-таблицы, такая функциональность обеспечивается Redisson.
Подобная реализация повышает сложность решения, в частности, устаревшие ключи не удаляются, если не запущен клиент.
Для удаления ключей при локальном хранении требуется платная версия Redisson, 
см. https://github.com/redisson/redisson/wiki/7.-distributed-collections#711-map-eviction-local-cache-and-data-partitioning.

## Вывод
Целесообразно использовать хранение элементов кэша в виде объектов ввиду простоты и надежности