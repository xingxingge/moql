/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.moql.core.join;

import org.moql.EntityMap;
import org.moql.EntityMapImpl;
import org.moql.core.Condition;
import org.moql.core.Join;
import org.moql.core.Queryable;
import org.moql.metadata.JoinMetadata;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Tang Tadin
 */
public class RightJoin extends AbstractJoin {
	
	protected EntityMap lNullEntityMap;

	public RightJoin(JoinMetadata joinMetadata,
			Queryable<? extends Object> queryable,
			Queryable<? extends Object> queryable2, Condition on) {
		super(joinMetadata, queryable, queryable2, on);
		// TODO Auto-generated constructor stub
		if (lTableName == null)
			lNullEntityMap = ((Join)lQueryable).getNullEntityMap();
	}

	@Override
	protected List<EntityMap> join() {
		// TODO Auto-generated method stub
		List<EntityMap> result = new LinkedList<EntityMap>();
		EntityMap entityMap;
		for(Iterator<? extends Object> rit = rQueryable.iterator(); rit.hasNext();) {
			Object rObj = rit.next();
			EntityMap rEntityMap = null;
			if (rTableName == null) {
				rEntityMap = (EntityMap)rObj;
			}
			boolean bMatch = false;
			for(Iterator<? extends Object> lit = lQueryable.iterator(); lit.hasNext();) {
				Object lObj = lit.next();
				entityMap = new EntityMapImpl();
				if (rTableName == null) {
					entityMap.putAll(rEntityMap);
				} else {
					entityMap.putEntity(rTableName, rObj);
				}
				if (lTableName == null) {
					entityMap.putAll((EntityMap)lObj);
				} else {
					entityMap.putEntity(lTableName, lObj);
				}
				if (on != null) {
					if (on.isMatch(entityMap)) {
						bMatch = true;
						result.add(entityMap);
					}
				} else {
					result.add(entityMap);
					bMatch = true;
				}
			}
			if (!bMatch) {
				entityMap = new EntityMapImpl();
				if (rTableName == null) {
					entityMap.putAll(rEntityMap);
				} else {
					entityMap.putEntity(rTableName, rObj);
				}
				if (lTableName == null) {
					entityMap.putAll(lNullEntityMap);
				} else {
					entityMap.putEntity(lTableName, null);
				}
				result.add(entityMap);
			}
		}
		return result;
	}

}
