package com.godarmed.microservice.defaultdemo.querydslDemo.model.queryEntity;

import com.godarmed.microservice.defaultdemo.querydslDemo.model.entity.Thotel;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Generated;
import java.io.Serializable;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

/**
 * QThotel is a Querydsl query type for Thotel
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QThotel extends EntityPathBase<Thotel> {

    private static final long serialVersionUID = 1938043029L;

    public static final QThotel thotel = new QThotel("thotel");

    public final StringPath address = createString("address");

    public final NumberPath<Integer> city = createNumber("city", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public QThotel(String variable) {
        super(Thotel.class, forVariable(variable));
    }

    public QThotel(Path<? extends Thotel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QThotel(PathMetadata metadata) {
        super(Thotel.class, metadata);
    }

}

