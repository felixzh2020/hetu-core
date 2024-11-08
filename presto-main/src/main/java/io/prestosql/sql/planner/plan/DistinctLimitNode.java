/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.prestosql.sql.planner.plan;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.prestosql.spi.plan.PlanNode;
import io.prestosql.spi.plan.PlanNodeId;
import io.prestosql.spi.plan.Symbol;

import javax.annotation.concurrent.Immutable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

@Immutable
public class DistinctLimitNode
        extends InternalPlanNode
{
    private final PlanNode source;
    private final long limit;
    private final boolean partial;
    private final List<Symbol> distinctSymbols;
    private final Optional<Symbol> hashSymbol;
    private AtomicLong atomicLimit;

    @JsonCreator
    public DistinctLimitNode(
            @JsonProperty("id") PlanNodeId id,
            @JsonProperty("source") PlanNode source,
            @JsonProperty("limit") long limit,
            @JsonProperty("partial") boolean partial,
            @JsonProperty("distinctSymbols") List<Symbol> distinctSymbols,
            @JsonProperty("hashSymbol") Optional<Symbol> hashSymbol)
    {
        super(id);
        this.source = requireNonNull(source, "source is null");
        checkArgument(limit >= 0, "limit must be greater than or equal to zero");
        this.limit = limit;
        this.atomicLimit = new AtomicLong(limit);
        this.partial = partial;
        this.distinctSymbols = ImmutableList.copyOf(distinctSymbols);
        this.hashSymbol = requireNonNull(hashSymbol, "hashSymbol is null");
        checkArgument(!hashSymbol.isPresent() || !distinctSymbols.contains(hashSymbol.get()), "distinctSymbols should not contain hash symbol");
    }

    @Override
    public List<PlanNode> getSources()
    {
        return ImmutableList.of(source);
    }

    @JsonProperty
    public PlanNode getSource()
    {
        return source;
    }

    @JsonProperty
    public long getLimit()
    {
        return limit;
    }

    @JsonProperty
    public boolean isPartial()
    {
        return partial;
    }

    @JsonProperty
    public Optional<Symbol> getHashSymbol()
    {
        return hashSymbol;
    }

    @JsonProperty
    public List<Symbol> getDistinctSymbols()
    {
        return distinctSymbols;
    }

    @Override
    public List<Symbol> getOutputSymbols()
    {
        ImmutableList.Builder<Symbol> outputSymbols = ImmutableList.builder();
        outputSymbols.addAll(distinctSymbols);
        hashSymbol.ifPresent(outputSymbols::add);
        return outputSymbols.build();
    }

    @Override
    public <R, C> R accept(InternalPlanVisitor<R, C> visitor, C context)
    {
        return visitor.visitDistinctLimit(this, context);
    }

    @Override
    public PlanNode replaceChildren(List<PlanNode> newChildren)
    {
        return new DistinctLimitNode(getId(), Iterables.getOnlyElement(newChildren), limit, partial, distinctSymbols, hashSymbol);
    }

    public AtomicLong getAtomicLimit()
    {
        return atomicLimit;
    }
}
