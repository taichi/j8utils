/*
 * Copyright 2015 kamekoopa
 *
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

package com.github.kamekoopa.j8utils.data;

import com.github.kamekoopa.j8utils.utils.FE1;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Option<A> implements Iterable<A> {

	private static final None none = new None();

	public static <A> Option<A> of(A a){
		if(a == null){
			return none();
		}else{
			return new Some<>(a);
		}
	}

	public static <A> Option<A> from(Optional<A> optional){
		if( optional == null || !optional.isPresent() ) {
			return none();
		}else{
			return of(optional.get());
		}
	}

	@SuppressWarnings("unchecked")
	public static <A> Option<A> none(){
		return none;
	}

	public abstract boolean isSome();

	public abstract boolean isNone();

	public abstract <B> Option<B> map(Function<A, B> f);

	public abstract <B> Option<B> mape(FE1<A, B> f) throws Exception;

	public abstract <B> Option<B> flatMap(Function<A, Option<B>> f);

	public abstract <B> Option<B> flatMape(FE1<A, Option<B>> f) throws Exception;

	public abstract <B> B fold(Supplier<B> none, Function<A, B> f);

	public abstract A getOrElse(Supplier<A> def);


	public static final class Some<A> extends Option<A> {

		private final A a;

		private Some(A a) {
			this.a = a;
		}

		@Override
		public boolean isSome() {
			return true;
		}

		@Override
		public boolean isNone() {
			return false;
		}

		@Override
		public <B> Option<B> map(Function<A, B> f) {
			return new Some<>(f.apply(a));
		}

		@Override
		public <B> Option<B> mape(FE1<A, B> f) throws Exception {
			return new Some<>(f.applye(a));
		}

		@Override
		public <B> Option<B> flatMap(Function<A, Option<B>> f) {
			return f.apply(a);
		}

		@Override
		public <B> Option<B> flatMape(FE1<A, Option<B>> f) throws Exception {
			return f.applye(a);
		}

		@Override
		public <B> B fold(Supplier<B> none, Function<A, B> f) {
			return f.apply(a);
		}

		@Override
		public A getOrElse(Supplier<A> def){
			return a;
		}

		@Override
		public Iterator<A> iterator() {
			return new Iterator<A>() {
				private boolean called = false;
				@Override
				public boolean hasNext() {
					if (!called){
						called = true;
						return true;
					}else{
						return false;
					}
				}

				@Override
				public A next() {
					return a;
				}
			};
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Some)) return false;

			Some some = (Some) o;

			return a.equals(some.a);
		}

		@Override
		public int hashCode() {
			return a.hashCode();
		}

		@Override
		public String toString() {
			return "Some("+a+")";
		}
	}

	public static final class None<A> extends Option<A> {
		private None(){}

		@Override
		public boolean isSome() {
			return false;
		}

		@Override
		public boolean isNone() {
			return true;
		}

		@Override
		public <B> Option<B> map(Function<A, B> f) {
			return none();
		}

		@Override
		public <B> Option<B> mape(FE1<A, B> f) throws Exception {
			return none();
		}

		@Override
		public <B> Option<B> flatMap(Function<A, Option<B>> f) {
			return none();
		}

		@Override
		public <B> Option<B> flatMape(FE1<A, Option<B>> f) throws Exception {
			return none();
		}

		@Override
		public <B> B fold(Supplier<B> none, Function<A, B> f) {
			return none.get();
		}

		@Override
		public A getOrElse(Supplier<A> def){
			return def.get();
		}

		@Override
		public Iterator<A> iterator() {
			return new Iterator<A>() {
				@Override
				public boolean hasNext() {
					return false;
				}

				@Override
				public A next() {
					throw new NoSuchElementException("none");
				}
			};
		}

		@Override
		public String toString() {
			return "None";
		}
	}
}
