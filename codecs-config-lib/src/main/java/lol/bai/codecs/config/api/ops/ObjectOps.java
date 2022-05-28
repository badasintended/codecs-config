package lol.bai.codecs.config.api.ops;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;

public class ObjectOps implements DynamicOps<Object> {

    public static final ObjectOps INSTANCE = new ObjectOps();

    protected ObjectOps() {
    }

    @Override
    public Object empty() {
        return null;
    }

    @Override
    public <U> U convertTo(DynamicOps<U> outOps, Object input) {
        if (input == null) {
            return outOps.empty();
        }

        if (input instanceof Map) {
            return convertMap(outOps, input);
        }

        if (input instanceof List) {
            return convertList(outOps, input);
        }

        if (input instanceof String) {
            return outOps.createString((String) input);
        }

        if (input instanceof Boolean) {
            return outOps.createBoolean((Boolean) input);
        }

        try {
            BigDecimal decimal = new BigDecimal(String.valueOf(input));

            try {
                final long l = decimal.longValueExact();

                if ((byte) l == l) {
                    return outOps.createByte((byte) l);
                }

                if ((short) l == l) {
                    return outOps.createShort((short) l);
                }

                if ((int) l == l) {
                    return outOps.createInt((int) l);
                }

                return outOps.createLong(l);
            } catch (final ArithmeticException e) {
                final double d = decimal.doubleValue();

                if ((float) d == d) {
                    return outOps.createFloat((float) d);
                }

                return outOps.createDouble(d);
            }
        } catch (NumberFormatException e) {
            return outOps.createDouble(((Number) input).doubleValue());
        }
    }

    @Override
    public DataResult<Number> getNumberValue(Object input) {
        if (input instanceof Number) {
            return DataResult.success((Number) input);
        }

        return DataResult.error("Not a number");
    }

    @Override
    public Object createNumeric(Number value) {
        return value;
    }

    @Override
    public Object createBoolean(boolean value) {
        return value;
    }

    @Override
    public DataResult<String> getStringValue(Object input) {
        if (input instanceof String) {
            return DataResult.success((String) input);
        }

        return DataResult.error("Not a string");
    }

    @Override
    public Object createString(String value) {
        return value;
    }

    @Override
    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    public DataResult<Object> mergeToList(Object list, Object value) {
        if (list == null) {
            return DataResult.success(ImmutableList.of(value));
        }

        if (list instanceof List) {
            List<Object> casted = (List<Object>) list;
            ImmutableList.Builder<Object> builder = ImmutableList.builderWithExpectedSize(casted.size() + 1);
            builder.addAll(casted);
            builder.add(value);
            return DataResult.success(builder.build());
        }

        return DataResult.error("Not a list");
    }

    @Override
    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    public DataResult<Object> mergeToList(Object list, List<Object> values) {
        if (list == null) {
            return DataResult.success(ImmutableList.copyOf(values));
        }

        if (list instanceof List) {
            List<Object> casted = (List<Object>) list;
            ImmutableList.Builder<Object> builder = ImmutableList.builderWithExpectedSize(casted.size() + values.size());
            builder.addAll(casted);
            builder.addAll(values);
            return DataResult.success(builder.build());
        }

        return DataResult.error("Not a list");
    }

    @Override
    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    public DataResult<Object> mergeToMap(Object map, Object key, Object value) {
        if (map == null) {
            return DataResult.success(ImmutableMap.of(key.toString(), value));
        }

        if (map instanceof Map) {
            Map<String, Object> casted = (Map<String, Object>) map;
            ImmutableMap.Builder<String, Object> builder = ImmutableMap.builderWithExpectedSize(casted.size() + 1);
            builder.putAll(casted);
            builder.put(key.toString(), value);
            return DataResult.success(builder.build());
        }

        return DataResult.error("Not a map");
    }

    @Override
    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    public DataResult<Object> mergeToMap(Object map, Map<Object, Object> values) {
        if (map == null || map instanceof Map) {
            ImmutableMap.Builder<String, Object> builder;

            if (map != null) {
                Map<String, Object> casted = (Map<String, Object>) map;
                builder = ImmutableMap.builderWithExpectedSize(casted.size() + values.size());
                builder.putAll(casted);
            } else {
                builder = ImmutableMap.builderWithExpectedSize(values.size());
            }

            values.forEach((k, v) -> builder.put(k.toString(), v));
            return DataResult.success(builder.build());
        }

        return DataResult.error("Not a map");
    }

    @Override
    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    public DataResult<Object> mergeToMap(Object map, MapLike<Object> values) {
        if (map == null || map instanceof Map) {
            ImmutableMap.Builder<String, Object> builder;

            if (map != null) {
                Map<String, Object> casted = (Map<String, Object>) map;
                builder = ImmutableMap.builderWithExpectedSize(casted.size());
                builder.putAll(casted);
            } else {
                builder = ImmutableMap.builder();
            }

            values.entries().forEach(p -> builder.put(p.getFirst().toString(), p.getSecond()));
            return DataResult.success(builder.build());
        }

        return DataResult.error("Not a map");
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataResult<Stream<Pair<Object, Object>>> getMapValues(Object input) {
        if (input instanceof Map) {
            return DataResult.success(((Map<Object, Object>) input).entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue())));
        }

        return DataResult.error("Not a map");
    }

    @Override
    public Object createMap(Stream<Pair<Object, Object>> map) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        map.forEach(p -> builder.put(p.getFirst().toString(), p.getSecond()));
        return builder.build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataResult<Stream<Object>> getStream(Object input) {
        if (input instanceof List) {
            return DataResult.success(((List<Object>) input).stream());
        }

        return DataResult.error("Not a list");
    }

    @Override
    public Object createList(Stream<Object> input) {
        ImmutableList.Builder<Object> builder = ImmutableList.builder();
        input.forEach(builder::add);
        return builder.build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object remove(Object input, String key) {
        if (input instanceof Map) {
            ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

            ((Map<String, Object>) input).forEach((k, v) -> {
                if (!k.equals(key)) {
                    builder.put(k, v);
                }
            });

            return builder.build();
        }

        return input;
    }

}
