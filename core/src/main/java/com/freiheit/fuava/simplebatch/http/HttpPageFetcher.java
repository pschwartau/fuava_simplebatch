package com.freiheit.fuava.simplebatch.http;

import java.io.InputStream;

import com.freiheit.fuava.simplebatch.fetch.PageFetcher;
import com.freiheit.fuava.simplebatch.result.Result;
import com.google.common.base.Function;

public class HttpPageFetcher<T> implements PageFetcher<T> {
    private final PagingRequestSettings<T> settings;
    private final HttpFetcher fetcher;
    private final Function<InputStream, T> converter;

    public HttpPageFetcher( HttpFetcher fetcher, PagingRequestSettings<T> settings, Function<InputStream, T> converter ) {
        this.settings = settings;
        this.fetcher = fetcher;
        this.converter = converter;
    }

    @Override
    public Result<PagingInput, T> fetch( int from, int pageSize ) {
        PagingInput pi = new PagingInput( from, pageSize );
        try {
            T fetched = fetcher.fetch( converter, settings.createFetchUri(from, pageSize), settings.getRequestHeaders() );
            return Result.success( pi, fetched );
        } catch ( Throwable t ) {
            return Result.failed( pi, t );
        }
    }

}