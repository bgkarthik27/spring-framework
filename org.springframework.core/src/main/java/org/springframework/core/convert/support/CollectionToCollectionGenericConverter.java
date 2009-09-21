/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.core.convert.support;

import java.util.Collection;

import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.core.convert.TypeDescriptor;

final class CollectionToCollectionGenericConverter implements GenericConverter {

	private final GenericConversionService conversionService;

	public CollectionToCollectionGenericConverter(GenericConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		Collection sourceCollection = (Collection) source;
		TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
		if (sourceElementType == TypeDescriptor.NULL) {
			sourceElementType = getElementType(sourceCollection);
		}
		TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
		if (sourceElementType == TypeDescriptor.NULL || sourceElementType.isAssignableTo(targetElementType)) {
			if (sourceType.isAssignableTo(targetType)) {
				return sourceCollection;
			} else {
				Collection targetCollection = CollectionFactory.createCollection(targetType.getType(), sourceCollection
						.size());
				targetCollection.addAll(sourceCollection);
				return targetCollection;
			}
		}
		Collection targetCollection = CollectionFactory.createCollection(targetType.getType(), sourceCollection.size());
		GenericConverter converter = this.conversionService.getConverter(sourceElementType, targetElementType);
		if (converter == null) {
			throw new ConverterNotFoundException(sourceElementType, targetElementType);
		}
		for (Object element : sourceCollection) {
			targetCollection.add(converter.convert(element, sourceElementType, targetElementType));
		}
		return targetCollection;
	}

	private TypeDescriptor getElementType(Collection collection) {
		for (Object element : collection) {
			if (element != null) {
				return TypeDescriptor.valueOf(element.getClass());
			}
		}
		return TypeDescriptor.NULL;
	}

}