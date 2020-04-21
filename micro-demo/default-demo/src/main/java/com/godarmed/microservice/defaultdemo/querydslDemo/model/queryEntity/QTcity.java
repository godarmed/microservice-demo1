package com.godarmed.microservice.defaultdemo.querydslDemo.model.queryEntity;

import com.godarmed.microservice.defaultdemo.querydslDemo.model.entity.Tcity;
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
 * QTcity is a Querydsl query type for Tcity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTcity extends EntityPathBase<Tcity> {

    private static final long serialVersionUID = 1586383466L;

    public static final QTcity tcity = new QTcity("tcity");

    public final StringPath country = createString("country");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath map = createString("map");

    public final StringPath name = createString("name");

    public final StringPath state = createString("state");

    public QTcity(String variable) {
        super(Tcity.class, forVariable(variable));
    }

    public QTcity(Path<? extends Tcity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTcity(PathMetadata metadata) {
        super(Tcity.class, metadata);
    }

}

