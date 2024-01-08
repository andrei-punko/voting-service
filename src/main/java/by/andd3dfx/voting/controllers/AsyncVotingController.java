package by.andd3dfx.voting.controllers;

import by.andd3dfx.voting.dto.response.CandidatesResponse;
import by.andd3dfx.voting.services.impl.VotingService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class AsyncVotingController {

    private final VotingService votingService;

    @GetMapping("/async/candidates")
    public CompletableFuture<CandidatesResponse> getCandidatesUsingFuture() {
        log.info("Request received");

        var completableFuture = CompletableFuture.supplyAsync(votingService::getCandidates);

        log.info("Servlet thread released");

        return completableFuture;
    }

    @GetMapping("/async/candidates2")
    public DeferredResult<CandidatesResponse> getCandidatesUsingDeferredResult() {
        log.info("Request received");

        var deferredResult = new DeferredResult<CandidatesResponse>();
        ForkJoinPool.commonPool().submit(() ->
                deferredResult.setResult(votingService.getCandidates())
        );

        log.info("Servlet thread released");

        return deferredResult;
    }
}
