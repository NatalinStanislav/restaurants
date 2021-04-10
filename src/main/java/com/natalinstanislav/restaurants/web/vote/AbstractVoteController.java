package com.natalinstanislav.restaurants.web.vote;

import com.natalinstanislav.restaurants.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractVoteController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected final VoteService voteService;

    public AbstractVoteController(VoteService voteService) {
        this.voteService = voteService;
    }
}
